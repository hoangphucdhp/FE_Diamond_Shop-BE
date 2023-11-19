package com.example.DATN_API.Service;

import com.example.DATN_API.Entity.StatusOrder;
import com.example.DATN_API.Reponsitories.StatusOrderReponsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusOrderService {
    @Autowired
    StatusOrderReponsitory statusOrderReponsitory;
    public StatusOrder save(StatusOrder statusOrder){
        return statusOrderReponsitory.save(statusOrder);
    }
}
