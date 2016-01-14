# pdfService

PDF service based on Java and wkhtmltopdf.
Uses wkhtmltopdf to generate PDF by posting URL or HTML to it.
PDF parameters and domain blocking are configured in a properties file.

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

#### wkhtmltopdf and screen resolution

wkhtmltopdf does the job in converting HTML documents to PDF, but is dependent on the screen resolution. So when everything looks right on your development server, everything is zoomed in on your production server. When your development server have say 1920x1080 screen resolution, your headless production server (which may not have a graphical interface) will probably have a lower resolution. Use a smaller zoom parameter value in your properties file to compensate for this.

## Inspired by

https://github.com/geekdenz/wkhtmltopdf-service
