# pdfService

PDF service based on Java and wkhtmltopdf.
Uses wkhtmltopdf to generate PDF by posting URL or HTML to it.
PDF parameters and domain blocking is configured in a properties file.

## External dependencies

### wkhtmltopdf

- Installation and documentation: http://wkhtmltopdf.org/
- Supports: Windows, Linux and OS X
- C Source code available
- Windows:
    ```
    Windows environment variable PATH: 
        SET PATH=%PATH%;C:\Program Files\wkhtmltopdf\bin;
    ```

## Inspired by

https://github.com/geekdenz/wkhtmltopdf-service
