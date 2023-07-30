package io.paysky.creditCardNfcReader.iso7816emv;

import io.paysky.creditCardNfcReader.enums.TagTypeEnum;
import io.paysky.creditCardNfcReader.enums.TagValueTypeEnum;


public interface ITag {

	enum Class {
		UNIVERSAL, APPLICATION, CONTEXT_SPECIFIC, PRIVATE
	}

	boolean isConstructed();

	byte[] getTagBytes();

	String getName();

	String getDescription();

	TagTypeEnum getType();

	TagValueTypeEnum getTagValueType();

	Class getTagClass();

	int getNumTagBytes();

}
