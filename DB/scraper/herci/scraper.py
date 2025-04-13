import requests
from bs4 import BeautifulSoup
import time
import pandas as pd
import csv

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'
}

base_url = "https://www.csfd.cz"
start_url = "https://www.csfd.cz/zebricky/vlastni-vyber/"
herci_vysledek = []

output_file = "herci_z_filmu.csv"

    # ✅ vytvoření hlavičky (jen na začátku)
#with open(output_file, "w", newline='', encoding="utf-8") as f:
 #   writer = csv.writer(f)
  #  writer.writerow(["herec"])


for page in range(88, 180):  # počet stránek uprav dle potřeby
    url = f"{start_url}?page={page}&filter=rlW0rKOyVwbjYPWipzyanJ4vBz51oTjfVzqyoaWyVwcoBI0fVayyLKWsMaWioFV6oaIfoPjvrJIupy90olV6oaIfoPjvLJA0o3VvBygqYPWxnKWyL3EipvV6J119"
    print(f"🔎 Načítám stránku: {url}")
    
    res = requests.get(url, headers=headers)
    soup = BeautifulSoup(res.content, "html.parser")


    for film in soup.select("h3.film-title-norating a.film-title-name"):
        film_url = base_url + film["href"]
        print(f"🎬 Zpracovávám film: {film.text.strip()}")

        try:
            res_film = requests.get(film_url, headers=headers)
            film_soup = BeautifulSoup(res_film.content, "html.parser")

            for div in film_soup.find_all("div"):
                nadpis = div.find("h4")
                if nadpis and "Hrají:" in nadpis.text:
                    odkazy = div.find_all("a")
                    for odkaz in odkazy:
                        jmeno = odkaz.get_text(strip=True)
                        if jmeno and "více" not in jmeno.lower():
                            with open(output_file, "a", newline='', encoding="utf-8") as f:
                                writer = csv.writer(f)
                                writer.writerow([jmeno])
                    break

        except Exception as e:
            print(f"⚠️ Chyba při zpracování {film_url}: {e}")

        time.sleep(1.2)

# Uložení do CSV
print("✅ Hotovo! Uloženo do herci_z_filmu.csv")
