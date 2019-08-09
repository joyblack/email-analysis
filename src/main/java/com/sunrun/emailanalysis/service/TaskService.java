package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;


}
