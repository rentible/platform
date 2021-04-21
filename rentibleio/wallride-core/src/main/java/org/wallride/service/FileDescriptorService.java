package org.wallride.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wallride.domain.FileDescriptor;
import org.wallride.repository.FileDescriptorRepository;

@Service
public class FileDescriptorService {

    @Autowired
    private FileDescriptorRepository fileDescriptorRepository;

    public FileDescriptor save(FileDescriptor fileDescriptor) {
        return fileDescriptorRepository.save(fileDescriptor);
    }

    public void delete(FileDescriptor fileDescriptor) {
        fileDescriptorRepository.delete(fileDescriptor);
    }
}
