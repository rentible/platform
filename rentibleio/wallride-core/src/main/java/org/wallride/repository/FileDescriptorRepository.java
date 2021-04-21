package org.wallride.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wallride.domain.FileDescriptor;

import java.io.Serializable;

@Repository
public interface FileDescriptorRepository extends JpaRepository<FileDescriptor, Serializable> {
}
