package entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YandexSpellerAnswer {

    public Integer code;

    public Integer pos;

    public Integer row;

    public Integer col;

    public Integer len;

    public String word;

    public List<String> s = new ArrayList<>();



}
