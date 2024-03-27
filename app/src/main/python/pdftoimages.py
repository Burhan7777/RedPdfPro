from pypdfium2 import pypdfium2


def pdf_to_images(file):
    pdf = pypdfium2.PdfDocument(file)

    for i in range(len(pdf)):
        page = pdf[i]
        image = page.render(scale=4).to_pil()
        image.save(f"/storage/emulated/0/Download/images_output_{i:03d}.jpg", format="jpeg")
