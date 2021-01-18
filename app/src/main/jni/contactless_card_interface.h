#ifndef CONTACTLESS_CARD_INTERFACE_H_
#define CONTACTLESS_CARD_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

#define CONTACTLESS_CARD_MODE_AUTO		0
#define CONTACTLESS_CARD_MODE_TYPE_A	1
#define CONTACTLESS_CARD_MODE_TYPE_B	2
#define CONTACTLESS_CARD_MODE_TYPE_C	3

#define CONTACTLESS_CARD_EVENT_FOUND_CARD		0
#define CONTACTLESS_CARD_EVENT_TIME_OUT			1
#define CONTACTLESS_CARD_EVENT_COMM_ERROR		2
#define CONTACTLESS_CARD_EVENT_USER_CANCEL		3

#define EMV_KERNAL_EVENT_APP_CANDIDATE_LIST	100
#define EMV_KERNAL_EVENT_APP_SELECTED 101
#define EMV_KERNAL_EVENT_APP_DATA_READ_COMPLETED 102
#define EMV_KERNAL_EVENT_DATA_AUTH_COMPLETED 103
#define EMV_KERNAL_EVENT_ID_CHECK 104
#define EMV_KERNAL_EVENT_ONLINE_PIN 105
#define EMV_KERNAL_EVENT_PIN_BY_PASS_CONFIRM 106
#define EMV_KERNAL_EVENT_PROCESS_ONLINE 107
#define EMV_KERNAL_EVENT_PROCESS_COMPLETED 108

/*
 * this is an inner event, user will never receive this event.
 */
#define CONTACTLESS_CARD_EVENT_NO_CARD			0xFF

/*
 * When we found a card, the event data is ATR.
 */
typedef void (*CONTACTLESS_CARD_NOTIFIER)(void* pUserData, int nEvent, unsigned char* pEventData, int nDataLength);

/*
 * Initialize the contactless card reader
 * @param[in] : CONTACTLESS_CARD_NOTIFIER fNotifier : it is called when some events happen.
 * @param[in] : void* pUserData : user data, it is the first parameter of call back function
 * @param[out] : int* pErrorCode : error code if return value is equal to zero
 * return value : == 0, error
 * 				  != 0 , correct handle
 */
typedef void* (*contactless_card_open)(CONTACTLESS_CARD_NOTIFIER fNotifier, void* pUserData, int* pErrorCode);
/*
 * Close the contactless card reader
 * @param[in] : int nHandle : handle of this card reader
 * return vlaue : >= 0, success
 * 				  < 0, error code
 */
typedef int (*contactless_card_close)(int nHandle);

/*
 * Start searching the contactless card
 * If you set the nCardMode is auto, reader will try to activate card in type A, type B and type successively;
 * If you set the nCardMode is type A, type B, or type C, reader only try to activate card in the specified way.
 * @param[in] : int nHandle : handle of this card reader
 * @param[in] : int nCardMode : possible value :
 * 								CONTACTLESS_CARD_MODE_AUTO
 *								CONTACTLESS_CARD_MODE_TYPE_A
 *								CONTACTLESS_CARD_MODE_TYPE_B
 *								CONTACTLESS_CARD_MODE_TYPE_C
 * @param[in] : int nFlagSearchAll : 0 : signal user if we find one card in the field
 * 									 1 : signal user only we find all card in the field
 * @param[in] : nTimeout_MS : time out in milliseconds.
 * 							  if nTimeout_MS is less then zero, the searching process is infinite.
 * 							  You can terminate it using the function of contactless_card_search_target_end.
 * return value : >= 0, success in starting the process.
 * 				  < 0 , error code
 */
typedef int (*contactless_card_search_target_begin)(int nHandle, int nCardMode, int nFlagSearchAll, int nTimeout_MS);
/*
 * Stop the process of searching card.
 * @param[in] : int nHandle : handle of this card reader
 */
typedef int (*contactless_card_search_target_end)(int nHandle);

/*
 * Attach the target before transmitting apdu command
 * In this process, the target(card) is activated and return ATR
 * @param[in] : int nHandle : handle of this card reader
 * @param[in] : unsigned char* pATRBuffer : ATR buffer, if you set it null, you can not get the data.
 * @param[in] : unsigned int nATRBufferLength : length of ATR buffer.
 * return value : >= 0, success, length of ATR.
 * 				  < 0 , error code
 */

typedef int (*contactless_card_attach_target)(int nHandle, unsigned char* pATRBuffer, unsigned int nATRBufferLength);
/*
 * Detach the target. If you want to send APDU again, you should attach it.
 * @param[in] : int nHandle : handle of this card reader
 * return value : >= 0, success, length of ATR.
 * 				  < 0 , error code
 */
typedef int (*contactless_card_detach_target)(int nHandle);
/*
 * Transmit APDU command and get the response
 * @param[in] : int nHandle : handle of this card reader
 * @param[in]		: unsigned char* pAPDU : command of APDU
 * @param[in]		: unsigned int nAPDULength : length of command of APDU
 * @param[out]		: unsigned char* pResponse : response of command of APDU
 * @param[in][out]	: unsigned int* pResponseLength : [in], buffer length of response
 * 													  [out], length of response
 * return value : >= 0, success
 * 				  < 0 , error code
 */

typedef int (*contactless_card_transmit)(int nHandle, unsigned char* pAPDU, unsigned int nAPDULength, unsigned char* pResponse, unsigned int *pResponseLength);

/*
 * Send control command.
 * @param[in] : int nHandle : handle of this card reader
 * @param[in] : unsigned int nCmdID : id of command
 * @param[in][out] : unsigned char* pCmdData : data associated with command
 * 					[in] : data associated with command, if no data, you can set it NULL
 * 					[out]: response data
 *
 * @param[in] : unsigned int nDataLength : data length of command
 * return value : >= 0, success, response data length if any.
 * 				  < 0 , error code
 */
typedef int (*contactless_card_send_control_command)(int nHandle, unsigned int nCmdID, unsigned char* pCmdData, unsigned int nDataLength);


#ifdef __cplusplus
}
#endif

#endif /* CONTACTLESS_CARD_INTERFACE_H_ */
