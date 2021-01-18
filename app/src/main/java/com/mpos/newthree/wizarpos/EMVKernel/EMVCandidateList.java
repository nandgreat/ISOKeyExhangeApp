package com.mpos.newthree.wizarpos.EMVKernel;

public class EMVCandidateList {
	byte aidLength;
	byte[] aid = new byte[16];   				// Application Identifier
	byte[] appLabel = new byte[16+1];			// Application Label
	byte[] appPreferredName = new byte[16+1];	// Application Preferred Name
	byte PriorityExist;				            // Application Priority exist flag
	byte appPriority;				            // Application Priority

}
