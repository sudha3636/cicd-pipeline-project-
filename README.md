End-to-end CI/CD pipeline demonstrating industry-standard DevOps practices.

## Tech Stack
- **App:** Java 17 + Spring Boot + Maven
- **Code Quality:** SonarCloud + JaCoCo
- **CI:** Jenkins + GitHub Actions
- **Container:** Docker + Docker Hub
- **Orchestration:** Kubernetes (Minikube)
- **CD:** ArgoCD (GitOps)
- **Server:** AWS EC2 t2.micro

## Pipeline Flow
Code Push → Maven Build → Tests → SonarQube → Docker Build → Push to Registry → ArgoCD → Kubernetes

## Branching Strategy
- `main` — production ready, triggers full pipeline
- `develop` — integration branch, triggers CI only
- `feature/*` — individual features, branch from develop
