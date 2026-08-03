# 🚀 TelemetryVault-Observability: Distributed Tracing & Centralized Logging Suite

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.0-green.svg)
![Spring Cloud Gateway](https://img.shields.io/badge/Spring_Cloud_Gateway-Reactive-blue.svg)
![Zipkin](https://img.shields.io/badge/Zipkin-Distributed_Tracing-cyan.svg)
![Prometheus](https://img.shields.io/badge/Prometheus-Metrics-red.svg)
![Grafana](https://img.shields.io/badge/Grafana-Monitoring-orange.svg)
![Docker](https://img.shields.io/badge/Docker-Compose-blue.svg)

> **Kurumsal Seviye Mikroservis İzlenebilirlik (Observability), Dağıtık İstek Takibi (Distributed Tracing) ve Merkezi Loglama Altyapısı.**

---

## 📌 Projeye Genel Bakış

**TelemetryVault-Observability**, modern mikroservis mimarilerinde performans darboğazlarını (latency bottlenecks), servis kesintilerini ve uygulama hatalarını tespit etmeyi kolaylaştıran uçtan uca izlenebilirlik (Observability) platformudur.

Bir istemci isteği **`api-gateway`** üzerinden sisteme girdiği andan itibaren benzersiz bir **`Trace ID`** ve **`Span ID`** ile etiketlenir. Bu kimlik bilgisi, mikroservisler arasındaki hem HTTP hem de **Apache Kafka** asenkron mesajlaşma akışlarında korunarak isteklerin nerede ne kadar süre harcadığını milisaniye hassasiyetinde görünür kılar.

---

## 🏛️ Mimari Tasarım & İzlenebilirlik Akışı

```
                  [ Client / Web Application ]
                                │
                                │ HTTP Requests
                                ▼
        ┌───────────────────────────────────────────────┐
        │           api-gateway (Port: 8080)            │
        │    Spring Cloud Gateway + Micrometer Trace    │
        └───────────────────────┬───────────────────────┘
                                │ (TraceID: a1b2c3d4)
                ┌───────────────┴───────────────┐
                ▼                               ▼
    ┌───────────────────────┐       ┌───────────────────────┐
    │     auth-service      │       │  vault-event-service  │
    │     (Port: 8081)      │       │     (Port: 8082)      │
    │  Spring Security 6    │       │    Kafka Producer     │
    └───────────────────────┘       └───────────┬───────────┘
                                                │ (Header TraceID)
                                                ▼
                                    ┌───────────────────────┐
                                    │  Apache Kafka Cluster │
                                    │ (file-vault-events)   │
                                    └───────────┬───────────┘
                                                │
                                                ▼
                                    ┌───────────────────────┐
                                    │     Zipkin Server     │
                                    │     (Port: 9411)      │
                                    └───────────────────────┘
```

---

## 🛠️ Temel Bileşenler & Teknolojiler

| Bileşen | Teknoloji | Açıklama |
| :--- | :--- | :--- |
| **API Gateway** | Spring Cloud Gateway (WebFlux) | Reaktif yönlendirme, Rate Limiting ve Trace ID enjeksiyonu. |
| **Auth Service** | Spring Security 6 & JJWT 0.12.5 | OAuth2 Authorization Server ve kullanıcı yönetimi. |
| **Event Service** | Spring Boot & Apache Kafka | Asenkron olay üretimi ve tüketimi (Producer / Consumer). |
| **Distributed Tracing** | Micrometer Tracing & Zipkin | Servisler arası senkron/asenkron istek takibi ve gecikme analizi. |
| **Metrics Collector** | Prometheus & Spring Actuator | JVM bellek kullanımı, CPU, HTTP oranları ve Kafka lag metrikleri. |
| **Visual Dashboard** | Grafana | Canlı sistem sağlığı ve performans takip panoları. |
| **Orkestrasyon** | Docker Compose | Tüm altyapının tek komutla ayağa kaldırılması. |

---

## ⚡ Öne Çıkan Teknik Özellikler

1. **Distributed Tracing (Dağıtık İstek Takibi):**
   - HTTP başlıkları (`traceparent`) ve Kafka mesaj başlıkları aracılığıyla kesintisiz `Trace ID` aktarımı.
   - Zipkin üzerinde milisaniyelik istek şelalesi (Waterfall chart) analizi.

2. **Merkezi Loglama & Yapılandırılmış Loglar:**
   - Logback JSON formatı ile her log satırına otomatik eklenen `traceId` ve `spanId` bilgisi.
   - Hatalı isteklerin doğrudan ilgili log satırı ile eşleştirilmesi.

3. **Canlı Sistem Metrikleri & Alarm Altyapısı:**
   - Micrometer ve Prometheus aracılığıyla 15 saniyelik periyotlarla toplanan JVM ve sistem metrikleri.
   - Grafana görselleştirme panoları.

---

## 🛠️ Kurulum & Çalıştırma (Docker Compose)

### Önkoşullar
* **Docker Desktop** (v24.0+)
* **Java 17 / JDK 17** (Yerel derleme için)
* **Maven** (v3.9+)

### Adım 1: Projeyi Klonlayın
```bash
git clone https://github.com/ahmetfurkankisacik/TelemetryVault-Observability.git
cd TelemetryVault-Observability
```

### Adım 2: Tüm Servisleri ve İzleme Altyapısını Başlatın
```bash
docker compose up --build -d
```

### Adım 3: Servis Portları ve Arayüzler
* **Zipkin Tracing UI:** [http://localhost:9411](http://localhost:9411)
* **Prometheus Dashboard:** [http://localhost:9090](http://localhost:9090)
* **Grafana Monitoring:** [http://localhost:3000](http://localhost:3000) *(User/Pass: admin/admin)*
* **API Gateway:** [http://localhost:8080](http://localhost:8080)

---

## 👨‍💻 Yazar & İletişim

* **Yazar:** Ahmet Furkan Kısacık (Bilgisayar Mühendisi & Yazılım Eğitmeni)
* **Kişisel Web Sitesi:** [ahmetfurkankisacik.com](https://ahmetfurkankisacik.com)
* **LinkedIn:** [linkedin.com/in/ahmetfurkankisacik](https://linkedin.com/in/ahmetfurkankisacik)
* **Medium:** [medium.com/@ahmetfurkankisacik](https://medium.com/@ahmetfurkankisacik)

---

*Licensed under the MIT License - 2026 Ahmet Furkan Kısacık.*
