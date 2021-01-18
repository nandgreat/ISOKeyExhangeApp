package com.mpos.newthree.wizarpos.emvsample.transaction;

public class TransInfo
{
	public byte   id; 
	public int    id_display_en;
	public byte   flag;

	public TransInfo()
	{
		id = -1;
	}

	/**
	 * @param n -
	 *          the FieldNumber
	 */
	public TransInfo(byte n)
	{
		id = n;
	}

	/**
	 * @param num -
	 *          fieldNumber
	 * @param -
	 *          fieldLen
	 * @param type -
	 *          fieldType
	 */
	public TransInfo(byte num, int id_disp_en, byte type)
	{
		id = num;
		this.id_display_en = id_disp_en;
		flag = type;
	}
}