package com.mpos.newthree.obj;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/**
 * Created by HP on 8/23/2017.
 */

public class GetJSONResponse implements Serializable{

    private List<ApplicationParameter> application_parameter;
    private List<Headers> header;
    private List<Content> content;

    public List<ApplicationParameter> getApplication_parameter() {
        return application_parameter;
    }

    public void setApplication_parameter(List<ApplicationParameter> application_parameter) {
        this.application_parameter = application_parameter;
    }

    public List<Headers> getHeader() {
        return header;
    }

    public void setHeader(List<Headers> header) {
        this.header = header;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Headers{
        String merchant_id,terminal_id,version,payment_gateway,host_ip,primary_color,secondary_color;
        String logo_url,key,receipt_logo;
        int host_port;

        public String getMerchant_id() {
            return merchant_id;
        }

        public void setMerchant_id(String merchant_id) {
            this.merchant_id = merchant_id;
        }

        public String getTerminal_id() {
            return terminal_id;
        }

        public void setTerminal_id(String terminal_id) {
            this.terminal_id = terminal_id;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getPayment_gateway() {
            return payment_gateway;
        }

        public void setPayment_gateway(String payment_gateway) {
            this.payment_gateway = payment_gateway;
        }


        public String getHost_ip() {
            return host_ip;
        }

        public void setHost_ip(String host_ip) {
            this.host_ip = host_ip;
        }

        public String getPrimary_color() {
            return primary_color;
        }

        public void setPrimary_color(String primary_color) {
            this.primary_color = primary_color;
        }

        public String getSecondary_color() {
            return secondary_color;
        }

        public void setSecondary_color(String secondary_color) {
            this.secondary_color = secondary_color;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getReceipt_logo() {
            return receipt_logo;
        }

        public void setReceipt_logo(String receipt_logo) {
            this.receipt_logo = receipt_logo;
        }

        public int getHost_port() {
            return host_port;
        }

        public void setHost_port(int host_port) {
            this.host_port = host_port;
        }
    }

    public class ApplicationParameter {
        String other_merchant_id,acq_id,address,audio_file,card_check,offence_service_charge;
        String other_service_charge,paper_type,printer,printer_name,transaction_url,trans_address;
        String trans_city,trans_state_country,update_interval, other_aliase,merchant_name;
        int conn_timeout,echo_interval;

        public String getOther_merchant_id() {
            return other_merchant_id;
        }

        public void setOther_merchant_id(String other_merchant_id) {
            this.other_merchant_id = other_merchant_id;
        }

        public String getAcq_id() {
            return acq_id;
        }

        public void setAcq_id(String acq_id) {
            this.acq_id = acq_id;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAudio_file() {
            return audio_file;
        }

        public void setAudio_file(String audio_file) {
            this.audio_file = audio_file;
        }

        public String getCard_check() {
            return card_check;
        }

        public void setCard_check(String card_check) {
            this.card_check = card_check;
        }

        public int getConn_timeout() {
            return conn_timeout;
        }

        public void setConn_timeout(int conn_timeout) {
            this.conn_timeout = conn_timeout;
        }

        public int getEcho_interval() {
            return echo_interval;
        }

        public String getMerchant_name() {
            return merchant_name;
        }

        public void setMerchant_name(String merchant_name) {
            this.merchant_name = merchant_name;
        }

        public void setEcho_interval(int echo_interval) {
            this.echo_interval = echo_interval;
        }

        public String getOffence_service_charge() {
            return offence_service_charge;
        }

        public void setOffence_service_charge(String offence_service_charge) {
            this.offence_service_charge = offence_service_charge;
        }

        public String getOther_service_charge() {
            return other_service_charge;
        }

        public void setOther_service_charge(String other_service_charge) {
            this.other_service_charge = other_service_charge;
        }

        public String getOther_aliase() {
            return other_aliase;
        }

        public void setOther_aliase(String other_aliase) {
            this.other_aliase = other_aliase;
        }

        public String getPaper_type() {
            return paper_type;
        }

        public void setPaper_type(String paper_type) {
            this.paper_type = paper_type;
        }

        public String getPrinter() {
            return printer;
        }

        public void setPrinter(String printer) {
            this.printer = printer;
        }

        public String getPrinter_name() {
            return printer_name;
        }

        public void setPrinter_name(String printer_name) {
            this.printer_name = printer_name;
        }

        public String getTransaction_url() {
            return transaction_url;
        }

        public void setTransaction_url(String transaction_url) {
            this.transaction_url = transaction_url;
        }

        public String getTrans_address() {
            return trans_address;
        }

        public void setTrans_address(String trans_address) {
            this.trans_address = trans_address;
        }

        public String getTrans_city() {
            return trans_city;
        }

        public void setTrans_city(String trans_city) {
            this.trans_city = trans_city;
        }

        public String getTrans_state_country() {
            return trans_state_country;
        }

        public void setTrans_state_country(String trans_state_country) {
            this.trans_state_country = trans_state_country;
        }

        public String getUpdate_interval() {
            return update_interval;
        }

        public void setUpdate_interval(String update_interval) {
            this.update_interval = update_interval;
        }
    }

    public class Content{
        String menu_id,transactionType,form,formFields,menu_name,icon_url;
        List<SubMenu> sub_menu;

        private Bitmap bitmap;

        public String getMenu_id() {
            return menu_id;
        }

        public void setMenu_id(String menu_id) {
            this.menu_id = menu_id;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public String getForm() {
            return form;
        }

        public void setForm(String form) {
            this.form = form;
        }

        public String getFormFields() {
            return formFields;
        }

        public void setFormFields(String formFields) {
            this.formFields = formFields;
        }

        public String getMenu_name() {
            return menu_name;
        }

        public void setMenu_name(String menu_name) {
            this.menu_name = menu_name;
        }

        public String getIcon_url() {
            return icon_url;
        }

        public void setIcon_url(String icon_url) {
            this.icon_url = icon_url;
        }

        public List<SubMenu> getSub_menu() {
            return sub_menu;
        }

        public void setSub_menu(List<SubMenu> sub_menu) {
            this.sub_menu = sub_menu;
        }

        public class SubMenu{
            String menu_id,menu_name,icon_url;
            String tbl_id,regcat_id,itemtype_id,itemtype_grp,itemtype_name,payitem_id,payitem_name;
            String payitem_amt,created,username,formid,vehicle_reg_descr,rev_code,itemtype_code,merchant_codes;
            String alias,etranzact_alias;
            List<Item> items;

            public String getMenu_id() {
                return menu_id;
            }

            public void setMenu_id(String menu_id) {
                this.menu_id = menu_id;
            }

            public String getMenu_name() {
                return menu_name;
            }

            public String getIcon_url() {
                return icon_url;
            }

            public String getPayitem_name() {
                return payitem_name;
            }

            public String getPayitem_amt() {
                return payitem_amt;
            }

            public String getItemtype_name() {
                return itemtype_name;
            }

            public String getTbl_id() {
                return tbl_id;
            }

            public String getRegcat_id() {
                return regcat_id;
            }

            public String getItemtype_id() {
                return itemtype_id;
            }

            public String getItemtype_grp() {
                return itemtype_grp;
            }

            public String getPayitem_id() {
                return payitem_id;
            }

            public String getCreated() {
                return created;
            }

            public String getUsername() {
                return username;
            }

            public String getFormid() {
                return formid;
            }

            public String getVehicle_reg_descr() {
                return vehicle_reg_descr;
            }

            public String getRev_code() {
                return rev_code;
            }

            public String getItemtype_code() {
                return itemtype_code;
            }

            public String getMerchant_codes() {
                return merchant_codes;
            }

            public String getAlias() {
                return alias;
            }

            public String getEtranzact_alias() {
                return etranzact_alias;
            }

            public List<Item> getItems() {
                return items;
            }

            public void setItems(List<Item> items) {
                this.items = items;
            }

            public class Item {
                String menu_id,tbl_id,regcat_id,itemtype_id,itemtype_grp,itemtype_name,payitem_id,payitem_name;
                String payitem_amt,created,username,formid,vehicle_reg_descr,rev_code,itemtype_code;
                String merchant_codes,alias,etranzact_alias;



                public String getMenu_id() {
                    return menu_id;
                }

                public void setMenu_id(String menu_id) {
                    this.menu_id = menu_id;
                }

                public String getTbl_id() {
                    return tbl_id;
                }

                public void setTbl_id(String tbl_id) {
                    this.tbl_id = tbl_id;
                }

                public String getRegcat_id() {
                    return regcat_id;
                }

                public void setRegcat_id(String regcat_id) {
                    this.regcat_id = regcat_id;
                }

                public String getItemtype_id() {
                    return itemtype_id;
                }

                public void setItemtype_id(String itemtype_id) {
                    this.itemtype_id = itemtype_id;
                }

                public String getItemtype_grp() {
                    return itemtype_grp;
                }

                public void setItemtype_grp(String itemtype_grp) {
                    this.itemtype_grp = itemtype_grp;
                }

                public String getItemtype_name() {
                    return itemtype_name;
                }

                public void setItemtype_name(String itemtype_name) {
                    this.itemtype_name = itemtype_name;
                }

                public String getPayitem_id() {
                    return payitem_id;
                }

                public void setPayitem_id(String payitem_id) {
                    this.payitem_id = payitem_id;
                }

                public String getPayitem_name() {
                    return payitem_name;
                }

                public void setPayitem_name(String payitem_name) {
                    this.payitem_name = payitem_name;
                }

                public String getPayitem_amt() {
                    return payitem_amt;
                }

                public void setPayitem_amt(String payitem_amt) {
                    this.payitem_amt = payitem_amt;
                }

                public String getCreated() {
                    return created;
                }

                public void setCreated(String created) {
                    this.created = created;
                }

                public String getUsername() {
                    return username;
                }

                public void setUsername(String username) {
                    this.username = username;
                }

                public String getFormid() {
                    return formid;
                }

                public void setFormid(String formid) {
                    this.formid = formid;
                }

                public String getVehicle_reg_descr() {
                    return vehicle_reg_descr;
                }

                public void setVehicle_reg_descr(String vehicle_reg_descr) {
                    this.vehicle_reg_descr = vehicle_reg_descr;
                }

                public String getRev_code() {
                    return rev_code;
                }

                public void setRev_code(String rev_code) {
                    this.rev_code = rev_code;
                }

                public String getItemtype_code() {
                    return itemtype_code;
                }

                public void setItemtype_code(String itemtype_code) {
                    this.itemtype_code = itemtype_code;
                }

                public String getMerchant_codes() {
                    return merchant_codes;
                }

                public void setMerchant_codes(String merchant_codes) {
                    this.merchant_codes = merchant_codes;
                }

                public String getAlias() {
                    return alias;
                }

                public void setAlias(String alias) {
                    this.alias = alias;
                }

                public String getEtranzact_alias() {
                    return etranzact_alias;
                }

                public void setEtranzact_alias(String etranzact_alias) {
                    this.etranzact_alias = etranzact_alias;
                }
            }
        }

    }
}
