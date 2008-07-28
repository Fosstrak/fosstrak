package org.fosstrak.hal.impl.feig.util;

import org.fosstrak.hal.util.ByteBlock;

public enum ISOTransponderResponseErrorCode {
	COMMAND_NOT_SUPPORTED(0x01, "Command not supported"),
	COMMAND_NOT_RECOGNIZED(0x02, "Command not recognized"),
	OPTION_NOT_SUPPORTED(0x03, "Option not supported"),
	UNKNOWN_ERROR(0x0f, "Unknown error"),
	BLOCK_NOT_AVAILABLE(0x10, "Block not available"),
	BLOCK_ALREADY_LOCKED(0x11, "Block already locked, cannot be locked again"),
	BLOCK_LOCKED(0x12, "Block locked, content cannot be changed"),
	PROGRAM_FAILED(0x13, "Block was not successfully programmed"),
	LOCK_FAILED(0x14, "Block was not successfully locked"),
	CUSTOM(0xa0, "Custom error"),
	RESERVED(0xf0, "Reserved");

	private final byte code;

	private final String description;

	ISOTransponderResponseErrorCode(int code, String description) {
		this.code = (byte) code;
		this.description = description;
	}

	public int code() {
		return ByteBlock.byteToNumber(code);
	}

	public String description() {
		return description;
	}

	public static ISOTransponderResponseErrorCode getResponseError(byte code) {

		if (code >= (byte) 0xa0 && code <= 0xdf)
			return ISOTransponderResponseErrorCode.CUSTOM;

		for (ISOTransponderResponseErrorCode t : ISOTransponderResponseErrorCode
				.values()) {
			if (t.code == code)
				return t;
		}

		return ISOTransponderResponseErrorCode.RESERVED;
	}

}