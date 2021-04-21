package hu.lanoga.flatshares.service;

import hu.lanoga.flatshares.model.FileDescriptor;
import hu.lanoga.flatshares.repository.FileDescriptorMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileDescriptorService implements CrudOperations<FileDescriptor> {

    private final FileDescriptorMapper fileDescriptorMapper;

    public FileDescriptorService(FileDescriptorMapper fileDescriptorMapper) {
        this.fileDescriptorMapper = fileDescriptorMapper;
    }

    @Override
    public int save(FileDescriptor entity) {
        return fileDescriptorMapper.save(entity);
    }

    @Override
    public int update(FileDescriptor entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileDescriptor findOne(int id) {
        return fileDescriptorMapper.findOne(id);
    }

    @Override
    public List<FileDescriptor> findAll() {
        return fileDescriptorMapper.findAll();
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }
}
