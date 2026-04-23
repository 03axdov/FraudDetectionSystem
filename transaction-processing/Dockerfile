FROM spark:python3

USER root

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

RUN mkdir -p /opt/spark/.ivy2 /opt/spark/.m2 && chmod -R 777 /opt/spark/.ivy2 /opt/spark/.m2

CMD ["/opt/spark/bin/spark-submit", \
     "--packages", "org.apache.spark:spark-sql-kafka-0-10_2.13:4.1.1,org.apache.spark:spark-avro_2.13:4.1.1", \
     "main.py"]