package top.atzlt.wordSeg.dictionary.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.atzlt.wordSeg.dictionary.Dictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HashSetDictionary implements Dictionary {

    private static final Logger logger = LoggerFactory.getLogger(HashSetDictionary.class);
    private Set<String> base = new HashSet<>();
    private int maxLength = -1;

    @Override
    public int getMaxLength() {
        return maxLength;
    }

    @Override
    public boolean contains(String item, int start, int length) {

        if(base==null){
            return false;
        }

        return base.contains(item.substring(start,length + start));

    }

    @Override
    public boolean contains(String item) {
        return base.contains(item);
    }

    @Override
    public void addAll(List<String> items) {

        for (String item : items) {
            if (maxLength < item.length()) {
                maxLength = item.length();
            }
            base.add(item);
        }
//        base.addAll(items);
    }

    @Override
    public void clear() {
        base.clear();
    }
}
