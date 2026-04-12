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
  
# CI/CD Pipeline Project

A full end-to-end CI/CD pipeline built with GitHub Actions, Docker, and cloud deployment.
# CI/CD Pipeline Project

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project&metric=coverage)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=sudha3636_cicd-pipeline-project&metric=bugs)](https://sonarcloud.io/summary/new_code?id=sudha3636_cicd-pipeline-project-)

## Tech Stack
- **App:** Node.js / Python
- **CI/CD:** GitHub Actions
- **Container:** Docker
- **Registry:** Docker Hub / GHCR
- **Deploy:** AWS EC2 / Render

## Pipeline Flow
Code Push → Lint → Test → Build Docker Image → Push to Registry → Deploy

## Getting Started
git clone https://github.com/sudha3636/cicd-pipeline-project.git
cd cicd-pipeline-project
npm install   # or pip install -r requirements.txt
npm test

## Branch Strategy
- `main` — production
- `develop` — integration
- `feature/*` — feature development
