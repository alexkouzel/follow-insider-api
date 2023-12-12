package com.followinsider.data.companies;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyLoaderTest {

    private static CompanyLoader loader = new CompanyLoader();

    @Test
    public void test() throws IOException {
        List<Company> companies = loader.loadCompanies();
        assertTrue(companies.size() > 10_000);
        verifyCompany(companies.get(0));
    }

    private void verifyCompany(Company company) {
        assertNotNull(company);
        assertNotNull(company.id());
        assertNotNull(company.exchange());
        assertNotNull(company.name());
        assertNotNull(company.symbol());
    }

}
