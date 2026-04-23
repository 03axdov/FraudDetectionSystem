import os
from dotenv import load_dotenv
import time
import requests

SCHEMA_REGISTRY_URL = os.getenv("SCHEMA_REGISTRY_URL")

class SchemaNotReady(Exception):
    pass

def get_latest_schema(subject: str) -> str:
    url = f"{SCHEMA_REGISTRY_URL}/subjects/{subject}/versions/latest"
    resp = requests.get(url, timeout=10)
    
    if resp.status_code == 404:
        try:
            payload = resp.json()
        except ValueError:
            resp.raise_for_status()

        if payload.get("error_code") == 40401:
            raise SchemaNotReady(f"Subject {subject} does not exist yet")
    
    resp.raise_for_status()
    return resp.json()["schema"]


def wait_for_latest_schema(subject: str, timeout_s: int = 60, interval_s: float = 2.0) -> str:
    deadline = time.monotonic() + timeout_s
    last_error = None

    while time.monotonic() < deadline:
        try:
            return get_latest_schema(subject)
        except SchemaNotReady as e:
            last_error = e
            time.sleep(interval_s)
        except requests.RequestException as e:
            last_error = e
            time.sleep(interval_s)

    raise RuntimeError(f"Schema for subject {subject} was not available within {timeout_s}s") from last_error