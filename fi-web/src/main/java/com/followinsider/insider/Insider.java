package com.followinsider.insider;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Insider {

    private String cik;

    private String name;

    private List<String> titles;

}
