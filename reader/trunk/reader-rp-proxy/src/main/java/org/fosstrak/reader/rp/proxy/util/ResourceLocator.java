/**
 * This class locates resources (configuration files, images) for the accada
 * reader proxy. It searches for user resources in the user directory (current
 * application directory) and for default resources in the library if no user
 * resources found.
 */
package org.accada.reader.rp.proxy.util;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * @author hallerj
 *
 */
public final class ResourceLocator {

   /**
    * Suffix for default resource file name if no name given.
    */
   private static final String DEFAULT_SUFFIX = "_default";

   /**
    * Get the URL of the resourceFileName.
    * 
    * @param resourceFileName
    *          path and name of the resource file (e.g. '/path/file.ext')
    * @param defaultResourceFileName
    *          path and name of the default resource file (e.g. '/path/file_default.ext')
    * @param caller
    *          the caller class
    * @return URL of the file or null if not found
    */
   public static URL getURL(String resourceFileName,
         String defaultResourceFileName, final Class caller) {
      // check arguments
      if (!resourceFileName.startsWith("/")) {
         resourceFileName = "/" + resourceFileName;
      }
      if (defaultResourceFileName == null) {
         defaultResourceFileName = resourceFileName.substring(0,
            resourceFileName.lastIndexOf(".")) + DEFAULT_SUFFIX
            + resourceFileName.substring(resourceFileName.lastIndexOf("."));
      }

      URL url = null;
      ClassLoader loader = getBestClassLoader(caller);

      // try user directory (current application directory)
      if (url == null) { 
         try {
            String userdirectorypath = "file:/" + System.getProperty("user.dir")
            .replace(System.getProperty("file.separator").charAt(0), '/')
            + resourceFileName;
            url = locateAbsolute(userdirectorypath);
         } catch (Exception e) {
         }
      }

      // try standard locate
      url = locate(resourceFileName, loader, caller);

      // try absolute file path
      if (url == null) {
         url = locateAbsolute("file:/" + resourceFileName.replace(
            System.getProperty("file.separator").charAt(0), '/'));
      }
      if (url == null) {
         // if adding leading slash was not appropriate
         url = locateAbsolute("file:/" + resourceFileName.substring(1).replace(
               System.getProperty("file.separator").charAt(0), '/'));
      }

      // try locate default
      if (url == null) {
         url = locateDefault(defaultResourceFileName, resourceFileName, caller);
      }

      // try standard locate of default config
      if (url == null) {
         url = locate(defaultResourceFileName, loader, caller);
      }

      return url;
   }

   /**
    * Tries to find the best ClassLoader for the caller Class.
    * 
    * @param caller
    *          the caller Class
    * @return
    *          ClassLoader found
    */
   private static ClassLoader getBestClassLoader(Class caller) {
      ClassLoader loader;
      
      // get possible class loaders
      final ClassLoader callerLoader = caller.getClassLoader();
      final ClassLoader contextLoader = Thread.currentThread()
         .getContextClassLoader();
      final ClassLoader systemLoader = ClassLoader.getSystemClassLoader();

      // choose best class loader
      if (isChild(contextLoader, callerLoader)) {
         loader = callerLoader;
      } else if (isChild(callerLoader, contextLoader)) {
         loader = contextLoader;
      } else {
         // the ambiguous case
         loader = contextLoader;
      }
      if ((loader != systemLoader) && isChild(loader, systemLoader)) {
         loader = systemLoader;
      }

      return loader;
   }

   /**
    * Test if 'child' is a delegation child of 'parent'.
    * This works only for classloaders that set their parent pointers correctly.
    * 'null' is interpreted as the primordial loader (everybody's parent).
    * 
    * @param parent
    *          the parent ClassLoader in question
    * @param child
    *          the child ClassLoader in question
    * @return
    *          true if 'child' is a delegation child of 'parent' (or if
    *          'parent'=='child')
    */
   private static boolean isChild (final ClassLoader parent, ClassLoader child)
   {
       if (parent == child) return true;
       if (child == null) return false;
       if (parent == null) return true;

       for ( ; child != null; child = child.getParent ())
       {
           if (child == parent) return true;
       }

       return false;
   }

   /**
    * Locate resource with help of a ClassLoader and caller Class.
    * 
    * @param resourceFileName
    *          the name of the resource file (e.g. '/path/name.ext')
    * @param loader
    *          a ClassLoader
    * @param caller
    *          the caller Class
    * @return
    *          URL of the file or null if not found
    */
   private static URL locate(String resourceFileName, ClassLoader loader,
         Class caller) {
      if (!resourceFileName.startsWith("/")) {
         resourceFileName = "/" + resourceFileName;
      }
      String shortResourceFileName = resourceFileName.substring(1,
            resourceFileName.length()); 
      URL url = null;
      
      // try to load resource
      if (loader != null) {
         url = loader.getResource(shortResourceFileName);
      }

      // try using caller class (can load from paths relative to caller package)
      if ((url == null) && (caller != null)) {
         url = caller.getResource(resourceFileName);
      }
      if ((url == null) && (caller != null)) {
         url = caller.getResource(shortResourceFileName);
      }

      // try getting as system resource from ClassLoader
      if (url == null) {
         url = ClassLoader.getSystemResource(shortResourceFileName);
      }

      return url;
   }

   /**
    * Locate resource on absolute path.
    * 
    * @param resourceFileName
    *          the path and file name of the resource
    * @return
    *          URL of the file or null if not found
    */
   private static URL locateAbsolute(String resourceFile) {
      URL url = null;

      boolean exists;
      try {
         exists = (new File(new URI(resourceFile))).exists();
      } catch (Exception e) {
         exists = false;
      }

      if (exists) {
         try {
            url = new URL(resourceFile);
         } catch (Exception e) {
            url = null;
         }
      }

      return url;
   }

   /**
    * Locate default resource where caller (library) is located.
    * 
    * @param defaultResourceFileName
    *          the name of the default resource
    * @param resourceFileName
    *          the name of the resource
    * @param caller
    *          the caller Class
    * @return
    *          URL of the file or null if not found
    */
   private static URL locateDefault(String defaultResourceFileName,
         String resourceFileName, Class caller) {
      if (!defaultResourceFileName.startsWith("/")) {
         defaultResourceFileName = "/" + defaultResourceFileName;
      }
      if (!resourceFileName.startsWith("/")) {
         resourceFileName = "/" + resourceFileName;
      }
      URL url = null;
      String urlstring;
      
      String codesourcelocation = caller.getProtectionDomain()
         .getCodeSource().getLocation().toString();
      if (codesourcelocation.endsWith("jar")) {
         // try beside or in jar containing the caller class
         urlstring = codesourcelocation.substring(0, codesourcelocation
            .lastIndexOf("/")) + resourceFileName;
         url = locateAbsolute(urlstring);
         if (url == null) {
            urlstring = codesourcelocation.substring(0, codesourcelocation
               .lastIndexOf("/")) + defaultResourceFileName;
            url = locateAbsolute(urlstring);
         }
         if (url == null) {
            urlstring = "jar:" + codesourcelocation + "!" + defaultResourceFileName;
            try {
               url = new URL(urlstring);
            } catch (Exception e) {
               url = null;
            }
         }
      } else {
         // try path containing the caller class
         urlstring = codesourcelocation.substring(0, codesourcelocation
            .lastIndexOf("/")) + resourceFileName;
         url = locateAbsolute(urlstring);
         if (url == null) {
            urlstring = codesourcelocation.substring(0, codesourcelocation
               .lastIndexOf("/")) + defaultResourceFileName;
            url = locateAbsolute(urlstring);
         }
      }
      
      return url;
   }

}
