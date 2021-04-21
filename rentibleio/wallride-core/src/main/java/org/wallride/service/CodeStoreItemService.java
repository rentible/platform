package org.wallride.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallride.domain.CodeStoreItem;
import org.wallride.repository.CodeStoreItemRepository;

import java.util.List;

@Service
public class CodeStoreItemService {

    @Autowired
    private CodeStoreItemRepository codeStoreItemRepository;

    public List<CodeStoreItem> findAll() {
        return codeStoreItemRepository.findAll();
    }

    public long count() {
        return codeStoreItemRepository.count();
    }

    public List<CodeStoreItem> findAllByCodeStoreTypeId(Integer id) {
        return codeStoreItemRepository.findAllByCodeStoreTypeId(id);
    }

    public CodeStoreItem findOneById(Integer id) {
        return codeStoreItemRepository.findOneById(id);
    }
}
