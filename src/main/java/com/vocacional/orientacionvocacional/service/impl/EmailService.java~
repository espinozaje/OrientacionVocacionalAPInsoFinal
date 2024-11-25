package com.vocacional.orientacionvocacional.service.impl;

import com.vocacional.orientacionvocacional.model.entity.Adviser;
import com.vocacional.orientacionvocacional.model.entity.Advisory;
import com.vocacional.orientacionvocacional.model.entity.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl mailSender;


    public void sendEmailWithAttachment(String name, String lastname, String Email, MultipartFile archivo) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        helper.setTo("jeisson12aaron@gmail.com");
        helper.setSubject("Solicitud de Postulación para el Cargo de Asesor de " + name + " " + lastname);

        // Personaliza el mensaje del correo
        String emailContent = String.format(
                "Hola,\n\n" +
                        "Me dirijo a usted con la intención de postularme para el cargo de asesor.\n\n" +
                        "Detalles del remitente:\n" +
                        "Nombre: %s %s\n" +
                        "Correo de contacto: %s\n\n" +
                        "Se adjunta el archivo PDF proporcionado.\n\n" +
                        "Saludos cordiales,\n" +
                        "Orientacion Vocacional",
                name, lastname, Email
        );

        helper.setText(emailContent);


        helper.addAttachment(archivo.getOriginalFilename(), new ByteArrayResource(archivo.getBytes()));


        mailSender.send(message);
    }


    public void sendHtmlEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.setFrom("jeisson12aaron@gmail.com");

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            e.printStackTrace();
        }
    }




    public String generateHtmlBodycreateAdvisory(Student student, Advisory advisory, Adviser adviser) {
        return "<html>" +
                "<head>" +
                "<style>" +
                "body {font-family: Arial, sans-serif;}" +
                "h2 {color: #4CAF50;}" +
                "p {font-size: 16px;}" +
                ".footer {color: #555555; font-size: 14px; text-align: center; padding-top: 20px; border-top: 1px solid #ddd;}" +
                ".footer a {color: #4CAF50; text-decoration: none;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<h2>¡Hola " + student.getFirstName() +" "+ student.getLastName()+ "!</h2>" +
                "<p>Se ha programado una nueva asesoría para ti. Aquí están los detalles:</p>" +
                "<table>" +
                "<tr><td><strong>Nombre de la Asesoría:</strong></td><td>" + advisory.getName() + "</td></tr>" +
                "<tr><td><strong>Link:</strong></td><td><a href='" + advisory.getLink() + "'>Acceder a la asesoría</a></td></tr>" +
                "<tr><td><strong>Fecha:</strong></td><td>" + advisory.getDate() + "</td></tr>" +
                "<tr><td><strong>Hora:</strong></td><td>" + advisory.getTime() + "</td></tr>" +
                "<tr><td><strong>Asesor:</strong></td><td>" + adviser.getFirstName()  +" "+ adviser.getLastName()+ "</td></tr>" +
                "</table>" +
                "<p>Te recomendamos acceder al link de la asesoría a la hora programada.</p>" +
                "<p>Si tienes alguna pregunta, no dudes en ponerte en contacto con nosotros.</p>" +

                "<div class='footer'>" +
                "<p>Atentamente,<br/>El equipo de Orientación Vocacional</p>" +
                "<p><a href='mailto:soporte@orientacionvocacional.com'>soporte@orientacionvocacional.com</a></p>" +
                "<p>Síguenos en nuestras redes sociales:</p>" +
                "<p><a href='https://www.facebook.com/OrientacionVocacional'>Facebook</a> | <a href='https://www.twitter.com/OrientacionVocacional'>Twitter</a></p>" +
                "<p><a href='https://www.instagram.com/OrientacionVocacional'>Instagram</a></p>" +
                "<img src='https://img.freepik.com/fotos-premium/longitud-total-estudiantes-universitarios-felices-caminando-juntos-campus_763111-5348.jpg' alt='Logo' width='150' style='display:block; margin-top:20px;'/>" +
                "</div>" +

                "</body>" +
                "</html>";
    }


    public void sendReprogrammingEmail(String to, String previousLink, String previousName, LocalDate previousDate, LocalTime previousTime, Advisory updatedAdvisory) {
        String subject = "Reprogramación de Asesoría";
        String body = "<h1>Su asesoría ha sido reprogramada</h1>"
                + "<p><strong>Datos anteriores:</strong></p>"
                + "<p>Nombre: " + previousName + "</p>"
                + "<p>Link: " + previousLink + "</p>"
                + "<p>Fecha: " + previousDate + "</p>"
                + "<p>Hora: " + previousTime + "</p>"
                + "<br>"
                + "<p><strong>Datos actualizados:</strong></p>"
                + "<p>Nombre: " + updatedAdvisory.getName() + "</p>"
                + "<p>Link: " + updatedAdvisory.getLink() + "</p>"
                + "<p>Fecha: " + updatedAdvisory.getDate() + "</p>"
                + "<p>Hora: " + updatedAdvisory.getTime() + "</p>";

        sendHtmlEmail(to, subject, body);
    }

    public void sendVerificationEmail(String toEmail, String verificationCode) {
        String subject = "Verificación de correo electrónico";
        String body = "Hola,\n\n" +
                "Gracias por registrarte. Por favor, usa el siguiente código para verificar tu correo electrónico:\n\n" +
                "Código de verificación: " + verificationCode + "\n\n" +
                "Si no solicitaste este registro, ignora este mensaje.\n\n" +
                "Gracias.";

        sendHtmlEmail(toEmail, subject, body);
    }

    public void sendCancellationEmail(String to, String name, String link, LocalDate date, LocalTime time) {
        String subject = "Cancelación de Asesoría";
        String body = "<h1>Su asesoría ha sido cancelada</h1>"
                + "<p>Detalles de la asesoría cancelada:</p>"
                + "<p>Nombre: " + name + "</p>"
                + "<p>Link: " + link + "</p>"
                + "<p>Fecha: " + date + "</p>"
                + "<p>Hora: " + time + "</p>"
                + "<br><p>Para más información, por favor contacte a su asesor.</p>";

        sendHtmlEmail(to, subject, body);
    }
}
