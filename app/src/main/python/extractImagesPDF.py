from pypdf import PdfReader


def extract_images(file):
    reader = PdfReader(file)
    try:
        for pages in range(len(reader.pages)):
            selected_page = reader.pages[pages]
            for images in selected_page.images:
                with open(f"/storage/emulated/0/Download/Pro Scanner/images/{images.name}", 'wb') as file:
                    file.write(images.data)

        return "Success"
    except Exception:
        return "Failure"
