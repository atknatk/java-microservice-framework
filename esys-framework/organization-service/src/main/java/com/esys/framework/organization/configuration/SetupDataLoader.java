package com.esys.framework.organization.configuration;

import com.esys.framework.core.entity.organization.*;
import com.esys.framework.core.entity.uaa.Authority;
import com.esys.framework.core.entity.uaa.Role;
import com.esys.framework.core.entity.uaa.User;
import com.esys.framework.organization.persistence.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Profile("!test")
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private MainGroupRepository mainGroupRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private BranchOfficeRepository branchOfficeRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;


    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        MainGroup mg1 = createMainGroup("Hayat Holding");
        Group tüketiciGr = createGroup("Tüketici" , mg1);
        Group agacGr = createGroup("Ağaç" , mg1);
        Group gayGr = createGroup("Gayrimenkul" , mg1);
        Group digerGr = createGroup("Diğer" , mg1);

        Company t1 = createCompany("Hayat Kimya San. A.Ş",tüketiciGr);
        Company t2 = createCompany("Sarl Hayat DHC Algerie",tüketiciGr);
        Company t3 = createCompany("Şirket-i Pars Hayat Sağlık Ürünleri Sehami Has",tüketiciGr);
        Company t4 = createCompany("Hayat Egypt Hygienic Products S.A.E",tüketiciGr);
        Company t5 = createCompany("Hayat Trading and Distribution S.A.E",tüketiciGr);
        Company t6 = createCompany("Hayat Kimya L.L.C.",tüketiciGr);
        Company t7 = createCompany("Hayat Marketing L.L.C",tüketiciGr);
        Company t8 = createCompany("Sarl Hayat Morocco",tüketiciGr);
        Company t9 = createCompany("Hayat Kimya Nigeria Ltd",tüketiciGr);
        Company t10 = createCompany("Hayat Bulgaria Chemical E.O.O.D.",tüketiciGr);
        Company t11 = createCompany("SC Hayat Chemical Rom S.R.L..",tüketiciGr);

        Company a1 = createCompany("Kastamonu Entegre Ağaç Sanayi ve Ticaret A.Ş.",agacGr);
        Company a2 = createCompany("Kastamonu Romania SA",agacGr);
        Company a3 = createCompany("Kastamonu Bulgaria AD",agacGr);
        Company a4 = createCompany("LLC Kastamonu Integrated Wood Industry",agacGr);
        Company a5 = createCompany("LLC Compass Engineering",agacGr);
        Company a6 = createCompany("KEAS International Cooperaief U.A.",agacGr);
        Company a7 = createCompany("KEAS Holding B.V.",agacGr);
        Company a8 = createCompany("Natron Hayat",agacGr);

        Company g1 = createCompany("Hayat Global Gayrimenkul A.Ş.",gayGr);
        Company g2 = createCompany("Hayat Emlak San. Yatırım A.Ş. ",gayGr);
        Company g3 = createCompany("Hayat Bayrampaşa Gayrimenkul A.Ş. ",gayGr);
        Company g4 = createCompany("Hayat Makine Sanayi Yatırım A.Ş.",gayGr);
        Company g5 = createCompany("Nokta İnşaat Yatırım Turizm San. Ve Tic A.Ş.",gayGr);

        Company d1 = createCompany("Hayat Enerji Elektrik Üretim San. Tic. A.Ş.",digerGr);
        Company d2 = createCompany("Hayat Havacılık ve Araç Kir. Ltd. Şti.",digerGr);
        Company d3 = createCompany("Hayat Proje Taahhüt ve İnşaat A.Ş.",digerGr);
        Company d4 = createCompany("Limaş Liman İşletmeciliği A.Ş.",digerGr);
        Company d5 = createCompany("AS Pazarlama A.Ş.",digerGr);

        BranchOffice br1 = createBranchOffice("İstanbul Merkez Ofisi",t1);
        BranchOffice br2 = createBranchOffice("İstanbul Fabrika",t1);
        BranchOffice br3 = createBranchOffice("Ankara Satış Ofisi",t1);
        BranchOffice br4 = createBranchOffice("Antalya Satış Ofisi",t1);

//        Employee e1 = createEmployee("Satış Ekibi",br1);
//        Employee e2 = createEmployee("Muhasebe Ekibi",br1);
//        Employee e3 = createEmployee("IK Ekibi",br1);
//        Employee e4 = createEmployee("İşçi Ekibi",br1);
//        Employee e5 = createEmployee("Hukuk Bürosu Ekibi",br1);
//
//        createCustomer("Esys LTD ŞTİ.",e1);
//        createCustomer("Esys Bilişim A.Ş.",e1);
//        createCustomer("Everva Bilişim A.Ş.",e1);


        alreadySetup = true;
    }

    private final MainGroup createMainGroup(final String name) {
        Optional<MainGroup> entity = mainGroupRepository.findByName(name);
        if (!entity.isPresent()) {
            MainGroup obj  = new MainGroup(name);
            return  mainGroupRepository.save(obj);
        }
        return entity.get();
    }


    private final Group createGroup(final String name, final MainGroup mg) {
        Optional<Group> entity = groupRepository.findByNameAndMainGroup(name,mg);
        if (!entity.isPresent()) {
            Group obj  = new Group(name);
            obj.setMainGroup(mg);
            return  groupRepository.save(obj);
        }
        return entity.get();
    }

    private final Company createCompany(final String name, final Group gr) {
        Optional<Company> entity = companyRepository.findByNameAndGroup(name,gr);
        if (!entity.isPresent()) {
            Company obj  = new Company();
            obj.setName(name);
            obj.setGroup(gr);
            return  companyRepository.save(obj);
        }
        return entity.get();
    }

    private final BranchOffice createBranchOffice(final String name, final Company c) {
        Optional<BranchOffice> entity = branchOfficeRepository.findByNameAndCompany(name,c);
        if (!entity.isPresent()) {
            BranchOffice obj  = new BranchOffice();
            obj.setName(name);
            obj.setCompany(c);
            return  branchOfficeRepository.save(obj);
        }
        return entity.get();
    }

//    private final Employee createEmployee(final String name, final BranchOffice c) {
//        Optional<Employee> entity = employeeRepository.findByNameAndBranchOffice(name,c);
//        if (!entity.isPresent()) {
//            Employee obj  = new Employee();
//            obj.setName(name);
//            obj.setBranchOffice(c);
//            return  employeeRepository.save(obj);
//        }
//        return entity.get();
//    }


    private final Customer createCustomer(final String name, final Employee c) {
        Optional<Customer> entity = customerRepository.findByNameAndEmployee(name,c);
        if (!entity.isPresent()) {
            Customer obj  = new Customer();
            obj.setName(name);
            obj.setEmployee(c);
            return  customerRepository.save(obj);
        }
        return entity.get();
    }

}
