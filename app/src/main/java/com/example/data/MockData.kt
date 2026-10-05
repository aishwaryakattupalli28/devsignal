package com.example.data

import com.example.model.ActionItem
import com.example.model.AnalyzedRepo
import com.example.model.ContributionStats
import com.example.model.DevProfile
import com.example.model.LanguageStat
import com.example.model.Priority
import com.example.model.ProgressSnapshot
import com.example.model.RecruiterInsights
import com.example.model.SubScore
import com.example.model.TargetRole

object MockData {

    fun generateGrid(seed: Int): List<List<Int>> {
        val weeks = mutableListOf<List<Int>>()
        var curSeed = seed
        for (w in 0 until 20) {
            val days = mutableListOf<Int>()
            for (d in 0 until 7) {
                curSeed = (curSeed * 9301 + 49297) % 233280
                val rand = curSeed % 10
                val level = when {
                    rand < 3 -> 0
                    rand < 6 -> 1
                    rand < 8 -> 2
                    rand < 9 -> 3
                    else -> 4
                }
                days.add(level)
            }
            weeks.add(days)
        }
        return weeks
    }

    fun getProfile(username: String, targetRole: TargetRole): DevProfile? {
        val normalized = username.trim().removePrefix("@").lowercase()
        if (normalized == "nouser123" || normalized == "unknown" || normalized == "notfound") {
            return null
        }

        return when {
            normalized.contains("sarah") -> createSarahProfile(targetRole)
            normalized.contains("chen") -> createChenProfile(targetRole)
            else -> createAlexProfile(targetRole)
        }
    }

    private fun createAlexProfile(targetRole: TargetRole): DevProfile {
        return when (targetRole) {
            TargetRole.SENIOR_FULLSTACK -> DevProfile(
                username = "alexrivera",
                fullName = "Alex Rivera",
                title = "Senior Software Engineer (Full-Stack)",
                location = "San Francisco, CA",
                publicRepos = 38,
                followers = 1420,
                following = 184,
                targetRole = targetRole,
                overallScore = 84,
                percentile = "Top 12% Candidate",
                fitLabel = "Strong Match",
                cadenceScore = SubScore(
                    category = "Code Cadence & Velocity",
                    score = 88,
                    tier = "Exceeds Expectations",
                    summary = "Consistent daily commits with disciplined branch management and fast PR cycle times.",
                    breakdownTags = listOf("18-day streak", "4.2 commits/wk", "PR merge avg: 14h")
                ),
                architectureScore = SubScore(
                    category = "Architecture & Breadth",
                    score = 82,
                    tier = "Meets Bar",
                    summary = "Strong microservice & frontend component patterns. Polyglot mix of TypeScript, Go, and Python.",
                    breakdownTags = listOf("GraphQL", "Next.js", "gRPC", "PostgreSQL")
                ),
                productionScore = SubScore(
                    category = "Production Readiness",
                    score = 79,
                    tier = "Near Benchmark",
                    summary = "Automated CI/CD via GitHub Actions present on flagship repos. Test coverage has minor gaps in core utilities.",
                    breakdownTags = listOf("Dockerized", "GitHub Actions", "Unit Tests 72%", "Dependabot")
                ),
                documentationScore = SubScore(
                    category = "Documentation & OSS",
                    score = 86,
                    tier = "Exceeds Expectations",
                    summary = "Excellent README visual diagrams, interactive API specs, clear onboarding guides, and active OSS contributions.",
                    breakdownTags = listOf("Architecture Diagrams", "MIT License", "Changelogs", "RFCs")
                ),
                recruiterInsights = RecruiterInsights(
                    headline = "High-velocity builder with end-to-end fullstack ownership and polished open source projects.",
                    strengths = listOf(
                        "Flagship repo 'hyper-cache' demonstrates advanced low-latency caching in Go with 1.2k GitHub stars.",
                        "Well-architected Next.js and TypeScript frontend design system in 'nexus-ui' with zero-bundle bloat.",
                        "Clear documentation and automated CI workflows across 4 major repositories."
                    ),
                    gaps = listOf(
                        "Test coverage in 'nexus-ui' sits at 68%, below the 80% benchmark expected for L5 roles.",
                        "Lack of published npm/Go release tags on recent microservice utilities."
                    ),
                    interviewTalkingPoints = listOf(
                        "Ask Alex about the concurrency handling and eviction strategy in 'hyper-cache'.",
                        "Probe architectural decisions behind state synchronization in 'nexus-ui'.",
                        "Explore how CI/CD pipelines are parameterized for staging vs production environments."
                    )
                ),
                languages = listOf(
                    LanguageStat("TypeScript", 46.5f, 0xFF3178C6, "184k LOC"),
                    LanguageStat("Go", 28.2f, 0xFF00ADD8, "112k LOC"),
                    LanguageStat("Python", 13.8f, 0xFF3572A5, "55k LOC"),
                    LanguageStat("Rust", 7.1f, 0xFFDEA584, "28k LOC"),
                    LanguageStat("Shell / Docker", 4.4f, 0xFF89E051, "17k LOC")
                ),
                contributionStats = ContributionStats(
                    totalContributionsLastYear = 1428,
                    currentStreakDays = 18,
                    longestStreakDays = 42,
                    weeklyCommitGrid = generateGrid(42)
                ),
                topRepos = listOf(
                    AnalyzedRepo(
                        name = "hyper-cache",
                        description = "High-throughput distributed in-memory cache written in Go with raft consensus and Prometheus metrics.",
                        stars = 1240,
                        forks = 142,
                        primaryLanguage = "Go",
                        languageColor = 0xFF00ADD8,
                        roleFitScore = 94,
                        hasCiCd = true,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Active (3 commits this week)",
                        tags = listOf("Distributed Systems", "Raft", "High Concurrency", "Prometheus"),
                        keyTakeaway = "Primary signal of distributed systems mastery. Clean code layout and benchmark suite."
                    ),
                    AnalyzedRepo(
                        name = "nexus-ui",
                        description = "Accessible, zero-runtime CSS-in-JS component library built for React 19 and Next.js App Router.",
                        stars = 890,
                        forks = 64,
                        primaryLanguage = "TypeScript",
                        languageColor = 0xFF3178C6,
                        roleFitScore = 91,
                        hasCiCd = true,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Active (Updated yesterday)",
                        tags = listOf("Design System", "WCAG 2.1 AA", "Storybook", "Turborepo"),
                        keyTakeaway = "Demonstrates frontend architectural leadership and deep understanding of modern React internals."
                    ),
                    AnalyzedRepo(
                        name = "event-mesh-k8s",
                        description = "Cloud-native event streaming operator for Kubernetes clusters with dead-letter queue automation.",
                        stars = 430,
                        forks = 38,
                        primaryLanguage = "Go",
                        languageColor = 0xFF00ADD8,
                        roleFitScore = 86,
                        hasCiCd = true,
                        hasTests = false,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Moderate (12 commits this month)",
                        tags = listOf("Kubernetes Operator", "Cloud Native", "Kafka"),
                        keyTakeaway = "Strong cloud DevOps capability; needs automated integration test coverage."
                    ),
                    AnalyzedRepo(
                        name = "data-pipeline-py",
                        description = "Asynchronous ETL data processing engine utilizing Polars and DuckDB for columnar transformations.",
                        stars = 215,
                        forks = 19,
                        primaryLanguage = "Python",
                        languageColor = 0xFF3572A5,
                        roleFitScore = 78,
                        hasCiCd = false,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Maintenance (Updated 2 months ago)",
                        tags = listOf("Data Engineering", "Polars", "AsyncIO"),
                        keyTakeaway = "Solid Python fundamentals; missing GitHub Actions CI workflow."
                    )
                )
            )

            TargetRole.STAFF_BACKEND -> DevProfile(
                username = "alexrivera",
                fullName = "Alex Rivera",
                title = "Senior Software Engineer (Full-Stack)",
                location = "San Francisco, CA",
                publicRepos = 38,
                followers = 1420,
                following = 184,
                targetRole = targetRole,
                overallScore = 78,
                percentile = "Top 24% Candidate",
                fitLabel = "Competitive Match",
                cadenceScore = SubScore(
                    category = "Code Cadence & Velocity",
                    score = 85,
                    tier = "Meets Bar",
                    summary = "Reliable commit frequency with balanced contributions across backend services.",
                    breakdownTags = listOf("18-day streak", "Go focus", "Detailed PR descriptions")
                ),
                architectureScore = SubScore(
                    category = "Architecture & Breadth",
                    score = 84,
                    tier = "Meets Bar",
                    summary = "Strong grasp of consensus algorithms and event streaming, but lacks explicit disaster-recovery specs.",
                    breakdownTags = listOf("Raft Consensus", "gRPC", "Kafka", "Postgres sharding")
                ),
                productionScore = SubScore(
                    category = "Production Readiness",
                    score = 71,
                    tier = "Action Required",
                    summary = "Staff roles demand end-to-end observability, load testing harness, and automated chaos testing.",
                    breakdownTags = listOf("Metrics present", "Missing load tests", "No chaos suite")
                ),
                documentationScore = SubScore(
                    category = "Documentation & OSS",
                    score = 76,
                    tier = "Meets Bar",
                    summary = "Good API documentation; could improve system architecture RFCs and benchmark reproducibility steps.",
                    breakdownTags = listOf("API docs", "Swagger/OpenAPI", "Needs RFC folder")
                ),
                recruiterInsights = RecruiterInsights(
                    headline = "Solid backend practitioner with strong systems fundamentals, needing deeper infrastructure reliability evidence for Staff level.",
                    strengths = listOf(
                        "Consensus and memory management in 'hyper-cache' are well above typical senior backend submissions.",
                        "Operator development in 'event-mesh-k8s' indicates real-world Kubernetes ecosystem competence."
                    ),
                    gaps = listOf(
                        "Staff rubric evaluates production load testing and failure domain isolation; current repos lack automated k6/load tests.",
                        "More evidence needed in database migration workflows and multi-region deployment automation."
                    ),
                    interviewTalkingPoints = listOf(
                        "Challenge Alex on raft partition tolerance scenarios in 'hyper-cache'.",
                        "Ask how distributed transactions and eventual consistency are handled in microservices."
                    )
                ),
                languages = listOf(
                    LanguageStat("Go", 44.0f, 0xFF00ADD8, "112k LOC"),
                    LanguageStat("TypeScript", 32.0f, 0xFF3178C6, "184k LOC"),
                    LanguageStat("Python", 14.0f, 0xFF3572A5, "55k LOC"),
                    LanguageStat("Rust", 7.0f, 0xFFDEA584, "28k LOC"),
                    LanguageStat("Shell", 3.0f, 0xFF89E051, "17k LOC")
                ),
                contributionStats = ContributionStats(
                    totalContributionsLastYear = 1428,
                    currentStreakDays = 18,
                    longestStreakDays = 42,
                    weeklyCommitGrid = generateGrid(42)
                ),
                topRepos = listOf(
                    AnalyzedRepo(
                        name = "hyper-cache",
                        description = "High-throughput distributed in-memory cache written in Go with raft consensus and Prometheus metrics.",
                        stars = 1240,
                        forks = 142,
                        primaryLanguage = "Go",
                        languageColor = 0xFF00ADD8,
                        roleFitScore = 95,
                        hasCiCd = true,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Active",
                        tags = listOf("Distributed Systems", "Raft", "High Concurrency"),
                        keyTakeaway = "Primary signal of backend depth."
                    ),
                    AnalyzedRepo(
                        name = "event-mesh-k8s",
                        description = "Cloud-native event streaming operator for Kubernetes clusters with dead-letter queue automation.",
                        stars = 430,
                        forks = 38,
                        primaryLanguage = "Go",
                        languageColor = 0xFF00ADD8,
                        roleFitScore = 89,
                        hasCiCd = true,
                        hasTests = false,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Moderate",
                        tags = listOf("Kubernetes", "Kafka", "Operators"),
                        keyTakeaway = "Infrastructure automation."
                    )
                )
            )

            TargetRole.LEAD_FRONTEND -> DevProfile(
                username = "alexrivera",
                fullName = "Alex Rivera",
                title = "Senior Software Engineer (Full-Stack)",
                location = "San Francisco, CA",
                publicRepos = 38,
                followers = 1420,
                following = 184,
                targetRole = targetRole,
                overallScore = 81,
                percentile = "Top 16% Candidate",
                fitLabel = "Strong Match",
                cadenceScore = SubScore(
                    category = "Code Cadence & Velocity",
                    score = 86,
                    tier = "Exceeds Expectations",
                    summary = "Steady component design iteration and responsive bug resolution.",
                    breakdownTags = listOf("Frequent releases", "Design token sync", "Micro-frontends")
                ),
                architectureScore = SubScore(
                    category = "Architecture & Breadth",
                    score = 88,
                    tier = "Exceeds Expectations",
                    summary = "Exceptional modern React 19 patterns, headless design components, state machines, and micro-frontends.",
                    breakdownTags = listOf("Zustand", "Tailwind", "Radix UI", "Web Vitals")
                ),
                productionScore = SubScore(
                    category = "Production Readiness",
                    score = 74,
                    tier = "Near Benchmark",
                    summary = "Visual regression and end-to-end Playwright tests should be added to flagship frontend repos.",
                    breakdownTags = listOf("Vitest", "Storybook", "Missing Playwright E2E")
                ),
                documentationScore = SubScore(
                    category = "Documentation & OSS",
                    score = 90,
                    tier = "Exceeds Expectations",
                    summary = "Storybook documentation, interactive component playgrounds, and zero-accessibility-violation badges.",
                    breakdownTags = listOf("Storybook 8", "WCAG 2.1 AA", "A11y automated audits")
                ),
                recruiterInsights = RecruiterInsights(
                    headline = "Proven frontend architect with world-class component design taste and web performance awareness.",
                    strengths = listOf(
                        "'nexus-ui' is a model design system implementation with outstanding documentation and TypeScript typing.",
                        "Excellent Web Vitals metrics and bundle size management."
                    ),
                    gaps = listOf(
                        "Missing automated Playwright E2E test runs on cross-browser matrices.",
                        "Accessibility audits need automated CI gating rather than periodic manual passes."
                    ),
                    interviewTalkingPoints = listOf(
                        "Discuss how Alex approaches headless component design and token governance in 'nexus-ui'.",
                        "Evaluate strategy for server-side rendering hydration optimization."
                    )
                ),
                languages = listOf(
                    LanguageStat("TypeScript", 58.0f, 0xFF3178C6, "184k LOC"),
                    LanguageStat("CSS/HTML", 18.0f, 0xFFE34C26, "45k LOC"),
                    LanguageStat("Go", 14.0f, 0xFF00ADD8, "112k LOC"),
                    LanguageStat("JavaScript", 10.0f, 0xFFF7DF1E, "32k LOC")
                ),
                contributionStats = ContributionStats(
                    totalContributionsLastYear = 1428,
                    currentStreakDays = 18,
                    longestStreakDays = 42,
                    weeklyCommitGrid = generateGrid(42)
                ),
                topRepos = listOf(
                    AnalyzedRepo(
                        name = "nexus-ui",
                        description = "Accessible, zero-runtime CSS-in-JS component library built for React 19 and Next.js App Router.",
                        stars = 890,
                        forks = 64,
                        primaryLanguage = "TypeScript",
                        languageColor = 0xFF3178C6,
                        roleFitScore = 96,
                        hasCiCd = true,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Active (Updated yesterday)",
                        tags = listOf("Design System", "WCAG 2.1 AA", "Storybook", "Turborepo"),
                        keyTakeaway = "Premier showcase of frontend craftsmanship."
                    )
                )
            )

            TargetRole.ML_SYSTEMS -> DevProfile(
                username = "alexrivera",
                fullName = "Alex Rivera",
                title = "Senior Software Engineer (Full-Stack)",
                location = "San Francisco, CA",
                publicRepos = 38,
                followers = 1420,
                following = 184,
                targetRole = targetRole,
                overallScore = 72,
                percentile = "Top 38% Candidate",
                fitLabel = "Moderate Match",
                cadenceScore = SubScore(
                    category = "Code Cadence & Velocity",
                    score = 78,
                    tier = "Meets Bar",
                    summary = "Python data engineering repo shows occasional bursts rather than continuous iteration.",
                    breakdownTags = listOf("Periodic commits", "Single author", "Clean git history")
                ),
                architectureScore = SubScore(
                    category = "Architecture & Breadth",
                    score = 74,
                    tier = "Near Benchmark",
                    summary = "Good data transformation logic, but missing ML training pipelines, PyTorch/TensorFlow models, or GPU inference kernels.",
                    breakdownTags = listOf("DuckDB", "Polars", "Missing PyTorch", "No Model Weights")
                ),
                productionScore = SubScore(
                    category = "Production Readiness",
                    score = 68,
                    tier = "Action Required",
                    summary = "Lacks ML evaluation metrics, model registry integrations (MLflow/Weights & Biases), and serving benchmarks.",
                    breakdownTags = listOf("Missing MLflow", "No GPU CI", "Missing data versioning")
                ),
                documentationScore = SubScore(
                    category = "Documentation & OSS",
                    score = 76,
                    tier = "Meets Bar",
                    summary = "Clear data pipeline architecture overview, but lacks model accuracy benchmarks and reproducibility seeds.",
                    breakdownTags = listOf("Data flow charts", "Needs benchmark tables")
                ),
                recruiterInsights = RecruiterInsights(
                    headline = "Competent software engineer with data processing aptitude, but lacks specialized ML modeling and deep learning systems depth.",
                    strengths = listOf(
                        "Data pipeline in 'data-pipeline-py' leverages fast columnar tools (Polars, DuckDB) effectively."
                    ),
                    gaps = listOf(
                        "No deep learning repositories or model inference serving engines (e.g. vLLM, TensorRT).",
                        "Absence of model evaluation frameworks or distributed training experience."
                    ),
                    interviewTalkingPoints = listOf(
                        "Ask Alex about vector database integrations and memory bottlenecks in large datasets.",
                        "Explore interest in transition path from backend systems to ML infrastructure."
                    )
                ),
                languages = listOf(
                    LanguageStat("Python", 42.0f, 0xFF3572A5, "55k LOC"),
                    LanguageStat("TypeScript", 32.0f, 0xFF3178C6, "184k LOC"),
                    LanguageStat("Go", 20.0f, 0xFF00ADD8, "112k LOC"),
                    LanguageStat("Rust", 6.0f, 0xFFDEA584, "28k LOC")
                ),
                contributionStats = ContributionStats(
                    totalContributionsLastYear = 1428,
                    currentStreakDays = 18,
                    longestStreakDays = 42,
                    weeklyCommitGrid = generateGrid(42)
                ),
                topRepos = listOf(
                    AnalyzedRepo(
                        name = "data-pipeline-py",
                        description = "Asynchronous ETL data processing engine utilizing Polars and DuckDB for columnar transformations.",
                        stars = 215,
                        forks = 19,
                        primaryLanguage = "Python",
                        languageColor = 0xFF3572A5,
                        roleFitScore = 82,
                        hasCiCd = false,
                        hasTests = true,
                        hasDocs = true,
                        hasLicense = true,
                        commitCadence = "Maintenance",
                        tags = listOf("Data Engineering", "Polars", "DuckDB"),
                        keyTakeaway = "High performance data manipulation."
                    )
                )
            )
        }
    }

    private fun createSarahProfile(targetRole: TargetRole): DevProfile {
        return DevProfile(
            username = "sarah-codes",
            fullName = "Sarah Chen",
            title = "Principal Frontend Architect",
            location = "Seattle, WA",
            publicRepos = 52,
            followers = 3890,
            following = 210,
            targetRole = targetRole,
            overallScore = if (targetRole == TargetRole.LEAD_FRONTEND) 93 else 82,
            percentile = "Top 4% Candidate",
            fitLabel = "Exceptional Match",
            cadenceScore = SubScore(
                category = "Code Cadence & Velocity",
                score = 94,
                tier = "Top Tier",
                summary = "Ultra-consistent contributor with 400+ merged PRs across open source libraries.",
                breakdownTags = listOf("34-day streak", "OSS Maintainer", "Quick code reviews")
            ),
            architectureScore = SubScore(
                category = "Architecture & Breadth",
                score = 92,
                tier = "Top Tier",
                summary = "Industry-standard design systems, AST compilers, and web performance optimization tools.",
                breakdownTags = listOf("TypeScript", "Compiler Plugins", "WebAssembly", "A11y")
            ),
            productionScore = SubScore(
                category = "Production Readiness",
                score = 88,
                tier = "Exceeds Expectations",
                summary = "Full matrix CI/CD across Node 18/20/22, automated visual regressions, and semantic releases.",
                breakdownTags = listOf("Playwright", "Changesets", "Codecov 91%", "Automated NPM publish")
            ),
            documentationScore = SubScore(
                category = "Documentation & OSS",
                score = 96,
                tier = "Top Tier",
                summary = "Comprehensive interactive docs, video tutorials, and active Discord community support.",
                breakdownTags = listOf("Fumadocs", "Live Playgrounds", "RFC Process", "Sponsor-backed")
            ),
            recruiterInsights = RecruiterInsights(
                headline = "Industry-recognized frontend leader with massive open source reach and pristine architecture standards.",
                strengths = listOf(
                    "Creator of 'vite-plugin-perf' used by over 40k weekly developers.",
                    "Exceptional 91% automated test coverage with full cross-browser test suites.",
                    "Proven ability to set company-wide frontend standards and mentorship."
                ),
                gaps = listOf(
                    "Backend microservices experience is limited primarily to Node.js/BFF architectures."
                ),
                interviewTalkingPoints = listOf(
                    "Discuss Sarah's methodology for scaling design systems across 30+ engineering squads.",
                    "Ask about compiler-level optimizations in modern frontend bundles."
                )
            ),
            languages = listOf(
                LanguageStat("TypeScript", 68.0f, 0xFF3178C6, "340k LOC"),
                LanguageStat("Rust", 18.0f, 0xFFDEA584, "90k LOC"),
                LanguageStat("CSS / SCSS", 10.0f, 0xFFE34C26, "50k LOC"),
                LanguageStat("Python", 4.0f, 0xFF3572A5, "20k LOC")
            ),
            contributionStats = ContributionStats(
                totalContributionsLastYear = 2340,
                currentStreakDays = 34,
                longestStreakDays = 89,
                weeklyCommitGrid = generateGrid(99)
            ),
            topRepos = listOf(
                AnalyzedRepo(
                    name = "vite-plugin-perf",
                    description = "Instant bundle-size and performance budget analysis plugin for Vite with flamegraph inspection.",
                    stars = 4210,
                    forks = 312,
                    primaryLanguage = "TypeScript",
                    languageColor = 0xFF3178C6,
                    roleFitScore = 98,
                    hasCiCd = true,
                    hasTests = true,
                    hasDocs = true,
                    hasLicense = true,
                    commitCadence = "Very Active",
                    tags = listOf("Vite", "Performance", "Developer Tooling"),
                    keyTakeaway = "Widely adopted developer tool demonstrating exceptional craft."
                ),
                AnalyzedRepo(
                    name = "design-tokens-compiler",
                    description = "Rust-based lightning fast transformer from Figma Variables to iOS, Android, and Web design tokens.",
                    stars = 1890,
                    forks = 98,
                    primaryLanguage = "Rust",
                    languageColor = 0xFFDEA584,
                    roleFitScore = 92,
                    hasCiCd = true,
                    hasTests = true,
                    hasDocs = true,
                    hasLicense = true,
                    commitCadence = "Active",
                    tags = listOf("Design Tokens", "Rust", "Multi-platform"),
                    keyTakeaway = "Cross-platform systems thinking."
                )
            )
        )
    }

    private fun createChenProfile(targetRole: TargetRole): DevProfile {
        return DevProfile(
            username = "chen-dev",
            fullName = "David Chen",
            title = "Senior Machine Learning Systems Engineer",
            location = "Austin, TX",
            publicRepos = 29,
            followers = 940,
            following = 130,
            targetRole = targetRole,
            overallScore = if (targetRole == TargetRole.ML_SYSTEMS) 90 else 80,
            percentile = "Top 7% Candidate",
            fitLabel = "Strong Match",
            cadenceScore = SubScore(
                category = "Code Cadence & Velocity",
                score = 87,
                tier = "Exceeds Expectations",
                summary = "Frequent commits focused on model evaluation runs, CUDA kernel optimizations, and inference servers.",
                breakdownTags = listOf("Daily commits", "Benchmarking suites", "ML reproducibility")
            ),
            architectureScore = SubScore(
                category = "Architecture & Breadth",
                score = 91,
                tier = "Top Tier",
                summary = "Deep mastery of PyTorch, Triton kernels, ONNX runtime, and distributed Ray clusters.",
                breakdownTags = listOf("PyTorch", "CUDA", "Triton", "Ray", "Docker GPU")
            ),
            productionScore = SubScore(
                category = "Production Readiness",
                score = 85,
                tier = "Exceeds Expectations",
                summary = "Robust automated latency regression benchmarks on synthetic GPU test harness.",
                breakdownTags = listOf("Triton Server", "Latency P99 < 12ms", "Prometheus", "DVC")
            ),
            documentationScore = SubScore(
                category = "Documentation & OSS",
                score = 84,
                tier = "Meets Bar",
                summary = "Clear mathematical formulas, model weight reproduction steps, and ablation study tables.",
                breakdownTags = listOf("Ablation Studies", "LaTeX formulas", "Weights & Biases reports")
            ),
            recruiterInsights = RecruiterInsights(
                headline = "Specialized AI inference optimization engineer capable of squeezing maximum throughput from model serving infrastructure.",
                strengths = listOf(
                    "Repository 'fast-llm-serve' demonstrates custom CUDA attention kernels with 3.2x speedup over standard HuggingFace.",
                    "Production Kubernetes deployment manifests with automated GPU autoscaling."
                ),
                gaps = listOf(
                    "Limited frontend experience outside of lightweight Gradio/Streamlit prototypes."
                ),
                interviewTalkingPoints = listOf(
                    "Discuss memory paging and KV-cache optimizations implemented in 'fast-llm-serve'.",
                    "Ask about multi-GPU distributed tensor parallelism trade-offs."
                )
            ),
            languages = listOf(
                LanguageStat("Python", 54.0f, 0xFF3572A5, "190k LOC"),
                LanguageStat("C++ / CUDA", 28.0f, 0xFFF34B7D, "98k LOC"),
                LanguageStat("Rust", 12.0f, 0xFFDEA584, "42k LOC"),
                LanguageStat("Shell", 6.0f, 0xFF89E051, "21k LOC")
            ),
            contributionStats = ContributionStats(
                totalContributionsLastYear = 1680,
                currentStreakDays = 22,
                longestStreakDays = 54,
                weeklyCommitGrid = generateGrid(77)
            ),
            topRepos = listOf(
                AnalyzedRepo(
                    name = "fast-llm-serve",
                    description = "Ultra low-latency LLM inference server supporting dynamic continuous batching and PagedAttention.",
                    stars = 2650,
                    forks = 230,
                    primaryLanguage = "Python",
                    languageColor = 0xFF3572A5,
                    roleFitScore = 96,
                    hasCiCd = true,
                    hasTests = true,
                    hasDocs = true,
                    hasLicense = true,
                    commitCadence = "Active",
                    tags = listOf("LLM Inference", "CUDA", "PagedAttention", "Triton"),
                    keyTakeaway = "Direct evidence of production AI serving competence."
                )
            )
        )
    }

    val initialActionItems = listOf(
        ActionItem(
            id = "act-1",
            title = "Add automated GitHub Actions CI & test coverage reporting to flagship repos",
            description = "Recruiters and engineering directors look for automated workflows on push. Adding a robust matrix test workflow immediately proves production discipline.",
            category = "CI/CD & DevOps",
            priority = Priority.P0_HIGH,
            scoreBoost = 4,
            completed = false,
            codeRecommendation = "name: CI\non: [push, pull_request]\njobs:\n  test:\n    runs-on: ubuntu-latest\n    steps:\n      - uses: actions/checkout@v4\n      - uses: actions/setup-go@v5\n      - run: go test -v -race -coverprofile=coverage.out ./..."
        ),
        ActionItem(
            id = "act-2",
            title = "Document system architecture with an interactive Mermaid diagram in README",
            description = "A visual architecture diagram bridges the gap between hiring managers and deep technical code. Shows systemic L5+ design capability.",
            category = "Documentation",
            priority = Priority.P0_HIGH,
            scoreBoost = 3,
            completed = true,
            codeRecommendation = "```mermaid\ngraph TD\n  Client[Web Client] -->|gRPC / HTTP2| Gateway[API Gateway]\n  Gateway --> Cache[(HyperCache Node)]\n  Cache --> DB[(PostgreSQL)]\n```"
        ),
        ActionItem(
            id = "act-3",
            title = "Raise unit test coverage from 68% to >80% on core utility modules",
            description = "Automate edge-case tests in parsing and eviction algorithms. Eliminates the #1 red flag identified in recruiter 30-second reviews.",
            category = "Testing",
            priority = Priority.P1_MEDIUM,
            scoreBoost = 3,
            completed = false,
            codeRecommendation = "npm test -- --coverage --coverageThreshold='{\"global\":{\"branches\":80,\"functions\":80,\"lines\":80}}'"
        ),
        ActionItem(
            id = "act-4",
            title = "Configure Dependabot & automated security vulnerability scanning",
            description = "Demonstrates proactive security posture. Prevents vulnerable trans-dependencies from lowering your production readiness index.",
            category = "Security",
            priority = Priority.P1_MEDIUM,
            scoreBoost = 2,
            completed = true,
            codeRecommendation = "version: 2\nupdates:\n  - package-ecosystem: 'npm'\n    directory: '/'\n    schedule:\n      interval: 'weekly'"
        ),
        ActionItem(
            id = "act-5",
            title = "Tag semantic release versions with changelog release notes",
            description = "Repositories with tagged releases (v1.0.0, v1.1.0) appear 3.4x more established than continuous un-versioned main branches.",
            category = "Code Hygiene",
            priority = Priority.P1_MEDIUM,
            scoreBoost = 2,
            completed = false,
            codeRecommendation = "git tag -a v1.2.0 -m \"Release v1.2.0 with raft consensus fixes\"\ngit push origin v1.2.0"
        ),
        ActionItem(
            id = "act-6",
            title = "Add a comprehensive CONTRIBUTING.md and standard Issue templates",
            description = "Signals open source maturity and team collaboration readiness for senior leadership positions.",
            category = "Documentation",
            priority = Priority.P2_LOW,
            scoreBoost = 1,
            completed = false,
            codeRecommendation = "# Contributing to HyperCache\n1. Fork the repository\n2. Create your feature branch\n3. Ensure all linter checks pass: `make lint`"
        ),
        ActionItem(
            id = "act-7",
            title = "Eliminate dead branches and stale unmerged draft pull requests",
            description = "Clean up abandoned experimental branches. Gives visitors an impression of tight project stewardship.",
            category = "Code Hygiene",
            priority = Priority.P2_LOW,
            scoreBoost = 1,
            completed = true,
            codeRecommendation = "git branch --merged main | grep -v 'main$' | xargs git branch -d"
        )
    )

    val initialSnapshots = listOf(
        ProgressSnapshot(
            id = "snap-1",
            scanDate = "Today, 10:45 AM",
            roleTitle = "Senior Full-Stack Engineer",
            overallScore = 84,
            testCoverage = 72,
            docClarity = 86,
            commitCadence = 88,
            deltaNote = "+5 pts (Mermaid docs added, Dependabot configured)",
            isCurrent = true
        ),
        ProgressSnapshot(
            id = "snap-2",
            scanDate = "Sep 22, 2026",
            roleTitle = "Senior Full-Stack Engineer",
            overallScore = 79,
            testCoverage = 66,
            docClarity = 78,
            commitCadence = 84,
            deltaNote = "+5 pts (Added hyper-cache benchmarks)",
            isCurrent = false
        ),
        ProgressSnapshot(
            id = "snap-3",
            scanDate = "Aug 14, 2026",
            roleTitle = "Senior Full-Stack Engineer",
            overallScore = 74,
            testCoverage = 58,
            docClarity = 64,
            commitCadence = 70,
            deltaNote = "Baseline Initial Audit",
            isCurrent = false
        )
    )
}
