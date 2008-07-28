package org.fosstrak.hal.impl.feig.util;

import org.fosstrak.hal.util.ByteBlock;

public enum StatusByte {
	// General status
	OK(0x00, "OK"),

	// Transponder status
	NO_TRANSPONDER(0x01, "No transponder"), DATA_FALSE(0x02, "Data false"), WRITE_ERROR(
			0x03, "Write error"), ADDRES_SERROR(0x04, "Address error"), WRONG_TRANSPONDER_TYPE(
			0x05, "Wrong transponder type"),

	// Parameter status
	EEPROM_FAILURE(0x10, "EEPROM failure"), PARAMETER_RANGE_ERROR(0x11,
			"Parameter range error"), FIRMWARE_ACTIVATION_REQUIRED(0x17,
			"Firmware activation required"),

	// Interface status
	UNKNOWN_COMMAND(0x80, "Unknown command"), LENGTH_ERROR(0x81, "Length error"), COMMAND_NA(
			0x82, "Command not available"), RF_COMMUNICATION_ERROR(0x83,
			"RF communication error"), MORE_DATA(0x94, "More data"), ISO_15693_ERROR(
			0x95, "ISO 15693 error");

	private final byte code;

	private final String description;

	StatusByte(int code, String description) {
		this.code = (byte) code;
		this.description = description;
	}

	public int code() {
		return ByteBlock.byteToNumber(code);
	}

	public String description() {
		return description;
	}

	public static StatusByte getStatus(byte code) {
		for (StatusByte t : StatusByte.values()) {
			if (t.code == code)
				return t;
		}
		return null;
	}

}