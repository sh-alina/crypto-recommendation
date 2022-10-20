package com.crypto.recommendation.repository;

import com.crypto.recommendation.common.CryptoRecord;
import com.crypto.recommendation.exception.CvsFileReadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

@Service
public class CvsFileCryptoRecordRepository implements CryptoRecordRepository {

    private final String directory;

    public CvsFileCryptoRecordRepository(@Value("${prices.directory}") String directory) {
        this.directory = directory;
    }

    @Override
    public Map<String, List<CryptoRecord>> read() {
        Map<String, List<CryptoRecord>> result = new HashMap<>();
        try (Stream<Path> paths = Files.list(Path.of(directory))) {
            paths.filter(Files::isRegularFile)
                    .forEach(path -> putRecordsFromFileToMap(result, path));
        } catch (IOException e) {
            throw new CvsFileReadException(format("Directory '%s' could not be read.", directory), e);
        }
        return result;
    }

    private void putRecordsFromFileToMap(Map<String, List<CryptoRecord>> result, Path path) {
        try {
            result.put(getCryptoCode(path), getCryptoRecordsFromFile(path));
        } catch (IOException e) {
            throw new CvsFileReadException(format("File %s could not be processed.", directory), e);
        }
    }

    private List<CryptoRecord> getCryptoRecordsFromFile(final Path csvPath) throws IOException {
        try (Stream<String> lines = Files.lines(csvPath)) {
            return lines.skip(1).map(this::getCryptoRecord).collect(Collectors.toList());
        }
    }

    private CryptoRecord getCryptoRecord(String line) {
        Pattern pattern = Pattern.compile(",");
        String[] arr = pattern.split(line);
        return new CryptoRecord(
                Long.parseLong(arr[0]),
                arr[1],
                (Double.parseDouble(arr[2])));
    }

    private String getCryptoCode(final Path path) {
        Pattern pattern = Pattern.compile("_");
        String[] arr = pattern.split(path.toFile().getName());
        return arr[0];
    }
}
