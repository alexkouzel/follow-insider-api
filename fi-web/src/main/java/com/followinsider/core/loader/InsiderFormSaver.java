package com.followinsider.core.loader;

import com.followinsider.core.model.InsiderForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InsiderFormSaver {

    @Transactional
    public void saveAll(List<InsiderForm> forms) {

    }

}
