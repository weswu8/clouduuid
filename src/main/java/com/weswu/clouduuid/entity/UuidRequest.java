package com.weswu.clouduuid.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UuidRequest {
    public String nameSpace;
    public String tag;
    public String description;

    public String getSpaceKey(){
        return this.nameSpace + "-" + this.tag;
    }

    public UuidRequest(String nameSpace, String tag){
        this.nameSpace = nameSpace;
        this.tag = tag;
    }
}
