import pytesseract
from PIL import Image


def ocr(file, name):
    img = Image.open(file)

    result = pytesseract.image_to_string(img)

    with open(f"/storage/emulated/0/Download/{name}.txt", "w") as file:
        file.write(result)