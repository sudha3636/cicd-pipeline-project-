# 🚀 End-to-End CI/CD Pipeline Project

[![CI Pipeline](https://github.com/sudha3636/cicd-pipeline-project-/actions/workflows/ci.yml/badge.svg)](https://github.com/sudha3636/cicd-pipeline-project-/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project-&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project-&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project-&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)

A production-grade CI/CD pipeline built with industry-standard DevOps tools — demonstrating automated build, test, containerization, GitOps deployment and Kubernetes orchestration on AWS.

---

## 🏗️ Architecture

```
Developer Push
      ↓
GitHub (main branch)
      ↓
┌─────────────────────────────────────┐
│         CI — GitHub Actions         │
│  Maven Build → JUnit Tests →        │
│  JaCoCo Coverage → SonarCloud       │
└─────────────────────────────────────┘
      ↓
┌─────────────────────────────────────┐
│         CD — Jenkins (local)        │
│  Clone → Build → Test →             │
│  Docker Build → Push to Hub →       │
│  Update k8s/deployment.yaml         │
└─────────────────────────────────────┘
      ↓
┌─────────────────────────────────────┐
│     GitOps — ArgoCD (local)         │
│  Watches GitHub k8s/ folder →       │
│  Auto-syncs to EC2 k3s cluster      │
└─────────────────────────────────────┘
      ↓
┌─────────────────────────────────────┐
│     AWS EC2 t3.micro (ap-south-1)   │
│  k3s Kubernetes → cicd-app pod      │
│  Exposed at :30080                  │
└─────────────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Layer | Tool | Purpose |
|---|---|---|
| Language | Java 21 + Spring Boot 3.2 | Application |
| Build | Apache Maven 3.9 | Compile & package |
| Testing | JUnit 5 + JaCoCo | Unit tests + coverage |
| Code Quality | SonarCloud | Quality gate (70% coverage) |
| CI | GitHub Actions | Automated CI pipeline |
| CD | Jenkins | CD orchestration |
| Container | Docker + Docker Hub | Image build & registry |
| GitOps | ArgoCD v3.3.8 | Continuous delivery |
| Kubernetes | k3s | Lightweight K8s on EC2 |
| Cloud | AWS EC2 t3.micro | ap-south-1 (Mumbai) |

---

## 📁 Project Structure

```
cicd-pipeline-project-/
├── .github/
│   └── workflows/
│       └── ci.yml              # GitHub Actions CI
├── k8s/
│   ├── deployment.yaml         # Kubernetes deployment
│   └── service.yaml            # NodePort service (:30080)
├── src/
│   ├── main/java/com/cicd/app/
│   │   ├── Application.java
│   │   ├── controller/
│   │   │   └── ItemController.java
│   │   └── model/
│   │       └── Item.java
│   └── test/java/com/cicd/app/
│       └── controller/
│           └── ItemControllerTest.java (11 tests)
├── Dockerfile                  # Container definition
├── Jenkinsfile                 # CD pipeline
├── pom.xml                     # Maven config
└── sonar-project.properties    # SonarCloud config
```

---

## 🔄 Complete Pipeline Flow

### CI Pipeline (GitHub Actions)
1. Developer pushes code to `main`
2. GitHub Actions triggers automatically
3. Maven compiles and runs 11 unit tests
4. JaCoCo generates coverage report (70%+ required)
5. SonarCloud scans for bugs, vulnerabilities, code smells
6. Quality Gate must pass ✅

### CD Pipeline (Jenkins + ArgoCD)
1. Jenkins detects push to `main`
2. Maven clean package → JAR built
3. Docker builds image → tagged with build number
4. Image pushed to Docker Hub (`sudha3636/cicd-app:latest`)
5. Jenkins updates `k8s/deployment.yaml` with new image tag
6. Updated YAML pushed to GitHub
7. ArgoCD detects change in `k8s/` folder
8. ArgoCD auto-syncs → deploys to k3s on EC2
9. Rolling update — zero downtime ✅
10. App live at `http://EC2-IP:30080` ✅

---

## 🚀 REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/items` | Get all items |
| GET | `/api/items/{id}` | Get item by ID |
| POST | `/api/items` | Create new item |
| PUT | `/api/items/{id}` | Update item |
| DELETE | `/api/items/{id}` | Delete item |
| GET | `/actuator/health` | App health check |

---

## 📸 Screenshots

### GitHub Actions CI — Passing
![GitHub Actions](docs/screenshots/01-github-actions-passing.png)

### SonarCloud Quality Gate — Passed
![SonarCloud](docs/screenshots/02-sonarcloud-quality-gate.png)

### Jenkins Pipeline — All Stages Green
![Jenkins](docs/screenshots/03-jenkins-pipeline.png)

### Docker Hub — Image Repository
![Docker Hub](docs/screenshots/04-dockerhub-image.png)

### ArgoCD — Healthy & Synced
![ArgoCD](docs/screenshots/05-argocd-synced.png)

### App Running on EC2
![App Live](docs/screenshots/06-app-live-ec2.png)

### AWS EC2 Instance
![EC2](docs/screenshots/07-ec2-instance.png)

### kubectl — Pods Running
![kubectl](docs/screenshots/08-kubectl-pods.png)

---

## ⚙️ Local Setup

```bash
# Clone the repo
git clone https://github.com/sudha3636/cicd-pipeline-project-.git
cd cicd-pipeline-project-

# Run tests
mvn test

# Build JAR
mvn clean package

# Run locally
java -jar target/cicd-app-1.0.0.jar

# Test endpoints
curl http://localhost:8080/api/items
curl http://localhost:8080/actuator/health
```

---

## 🐳 Docker

```bash
# Build image
docker build -t sudha3636/cicd-app:latest .

# Run container
docker run -p 8080:8080 sudha3636/cicd-app:latest

# Test
curl http://localhost:8080/actuator/health
```

---

## ☸️ Kubernetes

```bash
# Deploy to k3s
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Check pods
kubectl get pods
kubectl get svc

# Access app
curl http://<EC2-IP>:30080/api/items
```

---

## 🌿 Branching Strategy

| Branch | Purpose | Triggers |
|---|---|---|
| `main` | Production ready | Full CI + CD pipeline |
| `develop` | Integration branch | CI only |
| `feature/*` | Feature development | CI only |

---

## 👩‍💻 Author

**Sudha** — [GitHub](https://github.com/sudha3636)

---

*Built as a DevOps portfolio project demonstrating end-to-end CI/CD with industry-standard tools.*
