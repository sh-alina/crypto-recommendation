package com.crypto.recommendation.repository;

import com.crypto.recommendation.exception.CvsFileReadException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CvsFileCryptoRecordRepositoryTest {

    @Test
    void shouldRead() {
        CvsFileCryptoRecordRepository cvsFileCryptoRecordRepository = new CvsFileCryptoRecordRepository("prices");
        Assertions.assertNotNull(cvsFileCryptoRecordRepository.read());
    }

    @Test
    void shouldNotRead() {
        CvsFileCryptoRecordRepository cvsFileCryptoRecordRepository = new CvsFileCryptoRecordRepository("notSuchDir");
        Exception exception = assertThrows(CvsFileReadException.class, cvsFileCryptoRecordRepository::read);

        assertTrue(exception.getMessage().contains("Directory 'notSuchDir' could not be read."));
    }
}