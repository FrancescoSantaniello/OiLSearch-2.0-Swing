package francesco.santaniello.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

public class AutoCompleteService {

    public static final Path AUTOCOMPLETE_SOURCE_FILE_PATH = Path.of("./comuni.txt");
    private static final String LINE_SEPARATOR = System.lineSeparator();

    private final Set<String> autoCompleteSource = new HashSet<>();

    private static class InnerClass{
        private static final AutoCompleteService instance = new AutoCompleteService();
    }

    private AutoCompleteService(){
        try{
            autoCompleteSource.addAll(Files.readAllLines(AUTOCOMPLETE_SOURCE_FILE_PATH));
        }
        catch (IOException ex){}
    }

    public Set<String> getAutoCompleteSource(){
        return autoCompleteSource;
    }

    public static AutoCompleteService getInstance(){
        return InnerClass.instance;
    }

    public void add(String element){
        if (element != null && !element.isBlank() && autoCompleteSource.add(element)){
            try{
                Files.writeString(AUTOCOMPLETE_SOURCE_FILE_PATH, LINE_SEPARATOR + element, StandardOpenOption.APPEND);
            }
            catch (IOException ex){}
        }
    }
}
