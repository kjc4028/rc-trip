import requests
import socket
import threading
import time

from sentence_transformers import SentenceTransformer
from flask import Flask, request, jsonify

app = Flask(__name__)
model = SentenceTransformer('all-MiniLM-L6-v2')

EUREKA_SERVER = "http://localhost:8761/eureka"
APP_NAME = "embedding-service"
INSTANCE_PORT = 5000


@app.route('/embed', methods=['POST'])
def embed():
    data = request.json
    sentence = data['sentence']
    embedding = model.encode(sentence).tolist()
    return jsonify({'embedding': embedding})


def get_ip():
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        # Google DNS로 연결 시도하여 로컬 IP 확인
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
    except Exception:
        ip = "127.0.0.1"
    finally:
        s.close()
    return ip


def register_with_eureka():
    instance_id = f"{get_ip()}:{APP_NAME}:{INSTANCE_PORT}"
    url = f"{EUREKA_SERVER}/apps/{APP_NAME}"
    data = {
        "instance": {
            "hostName": get_ip(),
            "app": APP_NAME.upper(),
            "ipAddr": get_ip(),
            "vipAddress": APP_NAME,
            "secureVipAddress": APP_NAME,
            "status": "UP",
            "port": {"$": str(INSTANCE_PORT), "@enabled": "true"},
            "dataCenterInfo": {
                "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
                "name": "MyOwn"
            },
            "instanceId": instance_id
        }
    }
    headers = {"Content-Type": "application/json"}
    try:
        res = requests.post(url, json=data, headers=headers)
        print(f"Eureka 등록 결과: {res.status_code}")
    except Exception as e:
        print(f"Eureka 등록 실패: {e}")


def send_heartbeat():
    instance_id = f"{get_ip()}:{APP_NAME}:{INSTANCE_PORT}"
    url = f"{EUREKA_SERVER}/apps/{APP_NAME}/{instance_id}"
    while True:
        try:
            res = requests.put(url)
            print(f"Eureka 헬스체크: {res.status_code}")
        except Exception as e:
            print(f"Eureka 헬스체크 실패: {e}")
        time.sleep(30)  # 30초마다 갱신


def start_eureka_client():
    register_with_eureka()
    t = threading.Thread(target=send_heartbeat, daemon=True)
    t.start()

# 서비스 시작 시 아래 함수 호출
start_eureka_client()

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)