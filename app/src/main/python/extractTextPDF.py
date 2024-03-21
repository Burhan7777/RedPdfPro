from pypdf import PdfReader, PdfWriter


# def extract_text(file, name):
#     actual_text = ""
#     pdf1 = fitz.open(file)
#     for i in range(pdf1.page_count):
#         page = pdf1.load_page(i)
#         text = page.get_textpage('text')
#         actual_text += text.extractText()
#
#     with open(f"/storage/emulated/0/Download/{name}.txt", 'w') as file1:
#         file1.write(actual_text)

def extract_text_pypdf(file, name):
    actual_text = ""
    writer = PdfWriter()
    reader1 = PdfReader(file)
    for i in range(len(reader1.pages)):
        actual_text += reader1.pages[i].extract_text()

    try:
        with open(f"/storage/emulated/0/Download/{name}.txt", 'w') as file1:
            file1.write(actual_text)

            return "Success"
    except Exception:
        return "Failure"
