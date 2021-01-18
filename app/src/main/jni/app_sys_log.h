/*
 * Log.hpp
 *
 *  Created on: 2012-6-20
 *      Author: yaomaobiao
 */
#ifndef _APP_SYS_LOG_H_
#define _APP_SYS_LOG_H_

#ifdef __cplusplus
extern "C" {
#endif

void app_sys_error(const char* pMessage, ...);
void app_sys_warn(const char* pMessage, ...);
void app_sys_info(const char* pMessage, ...);
void app_sys_debug(const char* pMessage, ...);

void app_sys_dump(const char * str, unsigned char * pBuf, unsigned int len);

#ifdef __cplusplus
}
#endif

#endif /*_APP_SYS_LOG_H_*/
