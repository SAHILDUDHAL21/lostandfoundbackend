package com.sahil.lostandfound.config;

import com.sahil.lostandfound.entity.*;
import com.sahil.lostandfound.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScammerRepository scammerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String AVATAR_1 = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><circle cx='50' cy='50' r='50' fill='%2330281e'/><circle cx='50' cy='40' r='20' fill='%23faf5e9'/><path d='M20,85 C20,65 35,60 50,60 C65,60 80,65 80,85 Z' fill='%23faf5e9'/></svg>";
    private static final String AVATAR_2 = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><circle cx='50' cy='50' r='50' fill='%23574b3c'/><circle cx='50' cy='40' r='20' fill='%23faf5e9'/><path d='M20,85 C20,65 35,60 50,60 C65,60 80,65 80,85 Z' fill='%23faf5e9'/></svg>";
    private static final String AVATAR_3 = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 100 100'><circle cx='50' cy='50' r='50' fill='%2317120d'/><circle cx='50' cy='40' r='20' fill='%23faf5e9'/><path d='M20,85 C20,65 35,60 50,60 C65,60 80,65 80,85 Z' fill='%23faf5e9'/></svg>";
    
    private static final String ITEM_WALLET = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 400 300'><rect width='400' height='300' fill='%23f1eadb'/><rect x='60' y='60' width='280' height='180' rx='16' fill='%23574b3c' stroke='%2317120d' stroke-width='4'/><rect x='220' y='120' width='100' height='60' rx='8' fill='%23faf5e9' stroke='%2317120d' stroke-width='3'/><circle cx='250' cy='150' r='8' fill='%2317120d'/></svg>";
    private static final String ITEM_KEYS = "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 400 300'><rect width='400' height='300' fill='%23f1eadb'/><circle cx='150' cy='150' r='50' fill='none' stroke='%23574b3c' stroke-width='16'/><path d='M185 185 L280 280 L310 250 L290 230 L310 210 L270 170 Z' fill='%2317120d'/></svg>";

    @Override
    public void run(String... args) throws Exception {
        // Automatically alter existing PostgreSQL columns to TEXT to support unlimited base64 image strings
        try {
            jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN image TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN finder_avatar TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE items ALTER COLUMN description TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE claims ALTER COLUMN avatar TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE comments ALTER COLUMN avatar TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN avatar TYPE TEXT;");
            jdbcTemplate.execute("ALTER TABLE scammers ALTER COLUMN avatar TYPE TEXT;");
            System.out.println(">>> Successfully migrated all PostgreSQL image/avatar columns to TEXT!");
        } catch (Exception e) {
            System.out.println(">>> Database column check complete: " + e.getMessage());
        }

        if (itemRepository.count() == 0) {
            Item item1 = new Item(
                "FOUND",
                "Titan Leather Wallet & Delhi Metro Smart Card",
                "Found a brown bi-fold Titan leather wallet containing a Delhi Metro Smart Card near Connaught Place Metro station Gate 2 bench. Initial engraving PS on inner fold.",
                "Connaught Place Metro Station, New Delhi",
                "2 hours ago",
                ITEM_WALLET,
                "Priya Sharma",
                "@priya_s",
                AVATAR_1
            );
            item1 = itemRepository.save(item1);

            claimRepository.save(new Claim(
                item1.getId(),
                "Aarav Mehta",
                AVATAR_2,
                "1 hour ago",
                "PENDING_VERIFICATION",
                "Lost my brown Titan wallet near Gate 2 at 2 PM! Has my Delhi Metro pass ending in #4092 and HDFC debit card."
            ));

            claimRepository.save(new Claim(
                item1.getId(),
                "Ananya Iyer",
                AVATAR_3,
                "30 mins ago",
                "UNDER_REVIEW",
                "Might be my brother’s wallet. It has an initial PS stamp inside."
            ));

            commentRepository.save(new Comment(
                item1.getId(),
                "Rajesh Kumar",
                AVATAR_2,
                "1h 45m ago",
                "Did you hand it over to Delhi Metro CISF security desk or holding safe?"
            ));

            commentRepository.save(new Comment(
                item1.getId(),
                "Priya Sharma",
                AVATAR_1,
                "1h 30m ago",
                "Holding it safely! Please submit claim proof above with exact card details so we can arrange meet."
            ));

            Item item2 = new Item(
                "FOUND",
                "Royal Enfield Bike Key & Brass Ring",
                "Found a set of keys on a brass ring with a Royal Enfield leather key fob while walking near Bandra West Carter Road promenade.",
                "Bandra West Promenade, Mumbai",
                "4 hours ago",
                ITEM_KEYS,
                "Vikramaditya Reddy",
                "@vikram_r",
                AVATAR_3
            );
            item2 = itemRepository.save(item2);

            claimRepository.save(new Claim(
                item2.getId(),
                "Siddharth Kapoor",
                AVATAR_1,
                "2 hours ago",
                "PENDING_VERIFICATION",
                "Dropped my RE Meteor keys near Carter Road juice center at 5:30 PM! Key ring has a brown leather strap."
            ));

            commentRepository.save(new Comment(
                item2.getId(),
                "Sneha Kulkarni",
                AVATAR_2,
                "3 hours ago",
                "Handed to nearby police booth or holding onto it?"
            ));
        }

        if (scammerRepository.count() == 0) {
            scammerRepository.save(new ScammerProfile(
                "Rohan Verma",
                "@rohan_v99",
                AVATAR_1,
                4,
                "Fake Amazon Invoice & Unmatched Serial #",
                "Flagged Scammer"
            ));

            scammerRepository.save(new ScammerProfile(
                "Karan Malhotra",
                "@karan_m",
                AVATAR_2,
                3,
                "Incorrect Wallet Contents & Brand",
                "Suspicious Claimant"
            ));

            scammerRepository.save(new ScammerProfile(
                "Vikramaditya Singh",
                "@vikram_s",
                AVATAR_3,
                2,
                "Failed Engraving Verification",
                "Under Review"
            ));
        }
    }
}
