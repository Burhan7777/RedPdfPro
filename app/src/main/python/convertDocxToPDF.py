from docx2pdf import convert


def convert_docx_to_pdf(file, name):
    convert(file, f"/storage/emulated/0/Download/{name}.pdf")
