import pdftotext

pdffile = "WinZigC_Grammar.pdf"
txtfile = "WinZigC_Grammar.txt"

# Load your PDF
with open(pdffile, "rb") as f:
    pdf = pdftotext.PDF(f)
# Save all text to a txt file.
with open(txtfile, 'w') as f:
    f.write("\n\n".join(pdf))