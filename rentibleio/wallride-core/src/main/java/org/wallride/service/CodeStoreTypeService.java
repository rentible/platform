package org.wallride.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallride.domain.CodeStoreType;
import org.wallride.repository.CodeStoreTypeRepository;

import java.util.List;

@Service
public class CodeStoreTypeService {

    @Autowired
    private CodeStoreTypeRepository codeStoreTypeRepository;

    public long count() {
        return codeStoreTypeRepository.count();
    }

    public List<CodeStoreType> findAll() {
        return codeStoreTypeRepository.findAll();
    }
}
