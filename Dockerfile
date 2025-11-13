# Runtime image (JRE만)
FROM ubuntu:22.04

# 애플리케이션 실행 디렉토리
WORKDIR /app

# 크롤러 종속성 설치
USER root

RUN apt-get update && \
    apt-get install -y \
    openjdk-21-jdk \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libdrm2 \
    libdbus-1-3 \
    libxcb1 \
    libxkbcommon0 \
    libatspi2.0-0 \
    libx11-6 \
    libxcomposite1 \
    libxdamage1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libpango-1.0-0 \
    libcairo2 \
    --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*


# CI에서 만든 JAR 복사
COPY build/libs/*.jar /app/app.jar

# 실행
ENTRYPOINT ["java","-jar","/app/app.jar"]