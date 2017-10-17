package com.SDT.servicedog;

/**
 * Created by laitkor3 on 29/07/16.
 */
public class ClientProgram {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String id ;
    public String client_id ;
    public String program_id ;
    public String order ;
    public String tier ;
    public String status ;
}
