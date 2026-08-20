# Superhero Training Academy - Revised Implementation Plan (v3.0)

## Architecture Overview & Enhanced Scope

This plan integrates all project features and architectural enhancements:
1. **Dedicated Data Storage Directory (`data_files/`)**:
   - `data_files/heroes.txt`: All registered hero information (8-char IDs, names, levels, powers).
   - `data_files/powers.txt`: Master superpowers catalog (names & descriptions) dynamically loaded at runtime to assign 3–5 random powers to newly created heroes.
   - `data_files/financeSummary.txt`: Stores treasury snapshots (balance, total heroes, gross allowances, net allowances, total training costs, timestamps), fetched and displayed on the Finance Tab.
2. **Dedicated `id` Package (`src/id/IdGenerator.java`)**:
   - Generates and validates unique 8-character uppercase alphanumeric hero IDs (`[A-Z0-9]{8}`).
3. **Dedicated Hero Training Tab (`TrainingPanel`)**:
   - Standalone training station where user inputs an 8-digit Hero ID, views stats/cost/time, and trains the hero (+1 level).
4. **Interactive Threat Dispatch Radar (`ThreatPanel`)**:
   - Prominent **Red "Scan for Threat"** button.
   - No threat found $\rightarrow$ shows clear message and keeps the Red Scan Button visible.
   - Threat found $\rightarrow$ reveals threat details, Hero ID dispatch input, and "Dispatch Hero" button.
   - Catches `HeroNotEligibleException` on failure; on success pays reward and automatically resets back to the Red Scan Button.
5. **Java Swing Presentation Layer (4 Navigation Tabs)**:
   - Modern `MainFrame` with header treasury balance badge, `JTabbedPane` for the 4 tabs, and auto-persistence to `data_files/`.

---

## Directory & File Blueprint

```
SuperheroTrainingAcademy/
├── .gitignore
├── README.md
├── data_files/                             # [NEW DEDICATED DATA DIRECTORY]
│   ├── heroes.txt                          # Registered heroes database
│   ├── powers.txt                          # Powers master catalog (Name|Detail)
│   └── financeSummary.txt                  # Finance summary snapshot
│
└── src/
    ├── Main.java                           # Application Bootstrapper & GUI Entry Point
    ├── id/                                 # [IDENTITY PACKAGE]
    │   └── IdGenerator.java                # 8-digit uppercase alphanumeric ID generator
    ├── academy/                            # [DOMAIN MODEL]
    │   ├── Trainable.java                  # Formula interface
    │   ├── Hero.java                       # Hero entity with String ID & dynamic powers
    │   ├── Power.java                      # Power value object (name + detail)
    │   └── Academy.java                    # Hero roster & treasury aggregate root
    ├── threat/                             # [THREAT HIERARCHY]
    │   ├── Threat.java                     # Abstract superclass
    │   ├── FireThreat.java                 # Concrete Threat (Water, Level 2)
    │   ├── RobberyThreat.java              # Concrete Threat (Speed, Level 3)
    │   └── VillainThreat.java              # Concrete Threat (Strength, Level 5)
    ├── data/                               # [DATA ACCESS LAYER]
    │   └── DataManager.java                # Reads/writes all files in data_files/
    ├── finance/                            # [FINANCIAL ENGINE]
    │   ├── FinanceManager.java             # Allowance, tax, & cost calculations
    │   └── FinanceSummary.java             # Snapshot DTO for financeSummary.txt
    ├── exceptions/                         # [CUSTOM EXCEPTIONS]
    │   └── HeroNotEligibleException.java   # Checked exception for dispatch failures
    └── gui/                                # [SWING PRESENTATION LAYER]
        ├── MainFrame.java                  # Main Window (Header + 4 Tabs)
        ├── HeroPanel.java                  # Tab 1: Hero Roster (JTable + Add/Update/Delete)
        ├── TrainingPanel.java              # Tab 2: Training Station (ID Input + Train)
        ├── ThreatPanel.java                # Tab 3: Red Scan Button & Dispatch Station
        ├── FinancePanel.java               # Tab 4: Treasury Dashboard (Reads financeSummary.txt)
        └── UIUtils.java                    # Theme styling & Red Button style
```

---

## Step-by-Step Build Sequence

1. **Step 1: Storage Directory & Catalog Setup**
   - Create `data_files/` directory.
   - Seed `data_files/powers.txt` with master powers list (`Name|Detail`).
2. **Step 2: Identity & Model Refactor**
   - Create `id/IdGenerator.java` (`[A-Z0-9]{8}`).
   - Update `Power.java` (stores `name` and `detail`).
   - Update `Hero.java` (String ID, selects powers from loaded catalog).
   - Create `FinanceSummary.java` DTO.
3. **Step 3: Persistence Layer (`data/DataManager.java`)**
   - Build methods for `loadPowers()`, `saveHeroes()`, `loadHeroes()`, `saveFinanceSummary()`, and `loadFinanceSummary()`.
4. **Step 4: UI Foundation & Shell (`MainFrame.java`, `UIUtils.java`)**
   - Top banner with title and live Treasury Balance.
   - 4-Tab `JTabbedPane`.
5. **Step 5: Tab Implementation**
   - Implement `HeroPanel` (Tab 1: Table view, Add Hero with dynamic powers, Update, Delete).
   - Implement `TrainingPanel` (Tab 2: ID input, stats card, Train hero +1 level).
   - Implement `ThreatPanel` (Tab 3: Red Scan Button $\rightarrow$ Threat Card $\rightarrow$ ID Dispatch $\rightarrow$ Auto-Reset).
   - Implement `FinancePanel` (Tab 4: Reads and displays `data_files/financeSummary.txt`).
6. **Step 6: Auto-Save Verification**
   - Ensure every mutation (Add, Train, Delete, Dispatch) automatically updates both `heroes.txt` and `financeSummary.txt`.
