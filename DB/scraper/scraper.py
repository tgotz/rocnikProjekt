import requests
from bs4 import BeautifulSoup
import time
import pandas as pd

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
}

base_url = "https://www.csfd.cz"
start_url = "https://www.csfd.cz/zebricky/vlastni-vyber/"
film_names = []

for page in range(1, 15):
    url = f"{start_url}?page={page}&filter=rlW0rKOyVwbmYPWipzyanJ4vBz51oTjfVzqyoaWyVwcoBS0fVayyLKWsMaWioFV6oaIfoPjvrJIupy90olV6oaIfoPjvLJA0o3VvBygqYPWxnKWyL3EipvV6J119"
    print(f"Načítám stránku: {url}")

    response = requests.get(url, headers=headers)
    soup = BeautifulSoup(response.content, 'html.parser')

    for film in soup.select("h3.film-title-norating a.film-title-name"):
        name = film.text.strip()
        film_names.append(name)

    time.sleep(1.5)

# Uložit do CSV
df = pd.DataFrame(film_names, columns=["název"])
df.to_csv("best_thriller_serialy_nazvy_csfd.csv", index=False, encoding="utf-8")
print("Hotovo! Názvy uloženy v top_1000_nazvy_csfd.csv")
