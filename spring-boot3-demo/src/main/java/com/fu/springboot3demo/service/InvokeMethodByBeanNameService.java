package com.fu.springboot3demo.service;

import org.springframework.stereotype.Service;

@Service
public class InvokeMethodByBeanNameService {

    public String noParamsMethod(){
        return "invoke noParamsMethod by bean name.";
    }

    public String haveParamsMethod(String parma){
        return "invoke haveParamsMethod by bean name. parma = " + parma;
    }

}
