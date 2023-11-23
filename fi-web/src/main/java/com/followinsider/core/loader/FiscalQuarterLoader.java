package com.followinsider.core.loader;

import org.springframework.stereotype.Service;

@Service
public class FiscalQuarterLoader {

    // Every 2 hours, do the following:
    // - check available quarters on SEC
    // - check loaded quarters in the database
    // - load the latest non-loaded quarter
}
