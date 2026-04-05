# CI/CD Pipeline Project

A full end-to-end CI/CD pipeline built with GitHub Actions, Docker, and cloud deployment.

## Tech Stack
- **App:** Node.js / Python
- **CI/CD:** GitHub Actions
- **Container:** Docker
- **Registry:** Docker Hub / GHCR
- **Deploy:** AWS EC2 / Render

## Pipeline Flow
Code Push → Lint → Test → Build Docker Image → Push to Registry → Deploy

## Getting Started
git clone https://github.com/your-username/cicd-pipeline-project.git
cd cicd-pipeline-project
npm install   # or pip install -r requirements.txt
npm test

## Branch Strategy
- `main` — production
- `develop` — integration
- `feature/*` — feature development
