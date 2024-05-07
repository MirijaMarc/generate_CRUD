package util;

public class Constante {
    public static String ConfigurationDatabase = "./data/database.json";
    public static String ConfigurationPath = "./data/config.json";
    public static String ControllerTemplatePath = "./templates/ControllerTemplate.tmp";
    public static String ViewTemplatePath = "./templates/ViewTemplate.tmp";
    public static String RepoTemplatePath = "./templates/RepoTemplate.tmp";
    public static String DTOTemplatePath = "./templates/DTOTemplate.tmp";
    public static String EntityTemplatePath = "./templates/EntityTemplate.tmp";
    public static String InputDateTemplatePath= "./templates/view/add/InputDateTemplate.tmp";
    public static String InputDateTimeTemplatePath= "./templates/view/add/InputDateTimeTemplate.tmp";
    public static String InputNumberTemplatePath= "./templates/view/add/InputNumberTemplate.tmp";
    public static String InputTextTemplatePath= "./templates/view/add/InputTextTemplate.tmp";
    public static String InputSelectTemplatePath= "./templates/view/add/InputSelectTemplate.tmp";
    public static String UtilTemplatePath="./templates/UtilTemplate.tmp";

    public static String InputDateTemplatePathUpdate= "./templates/view/update/InputDateTemplate.tmp";
    public static String InputDateTimeTemplatePathUpdate= "./templates/view/update/InputDateTimeTemplate.tmp";
    public static String InputNumberTemplatePathUpdate= "./templates/view/update/InputNumberTemplate.tmp";
    public static String InputTextTemplatePathUpdate= "./templates/view/update/InputTextTemplate.tmp";
    public static String InputSelectTemplatePathUpdate= "./templates/view/update/InputSelectTemplate.tmp";
    public static String InputHiddenTemplatePath= "./templates/view/update/InputHiddenTemplate.tmp";


    public static String IMPORT_CONTROLLER = """
        import java.time.LocalDate;
        import java.time.format.DateTimeFormatter;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Optional;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.PageRequest;
        import org.springframework.data.domain.Pageable;
        import org.springframework.http.MediaType;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.GetMapping;
        import org.springframework.web.bind.annotation.ModelAttribute;
        import org.springframework.web.bind.annotation.PostMapping;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.multipart.MultipartFile;
        import org.springframework.web.servlet.ModelAndView;
        import [package].util.Util;
        import jakarta.persistence.EntityManager;
        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.persistence.PersistenceContext;
        import jakarta.servlet.http.HttpServletRequest;
        import jakarta.servlet.http.HttpServletResponse;
            """;

    public static String IMPORT_DTO = """
        import lombok.Data;
            """;


    public static String IMPORT_ENTITY = """
        import java.time.LocalDate;
        import jakarta.persistence.Column;
        import jakarta.persistence.Entity;
        import jakarta.persistence.EntityManager;
        import jakarta.persistence.GeneratedValue;
        import jakarta.persistence.GenerationType;
        import jakarta.persistence.Id;
        import jakarta.persistence.Query;
        import jakarta.persistence.Table;
        import jakarta.persistence.JoinColumn;
        import jakarta.persistence.ManyToOne;
        import lombok.Data;
            """;

    public static String IMPORT_REPO = """
        import java.util.List;
        import org.springframework.data.domain.Page;
        import org.springframework.data.domain.Pageable;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.stereotype.Repository;
            """;

    public static String ATTRIBUT_PRIMARY = """
                @Id
                @Column(name = "%s")
                @GeneratedValue(strategy = GenerationType.IDENTITY)
                private %s id;

            """;
    public static String ATTRIBUT_JOIN = """
                @ManyToOne
                @JoinColumn(name="%s")
                private %s %s;

            """;

    public static String ATTRIBUT = """
                @Column(name="%s")
                private %s %s;

            """;

    public static String FIELD_DTO = "     private %s %s;\n";

    public static String FIELD_CONT ="""
                @Autowired
                private [NomModeleUpper]Repository [NomModeleLower]Repository;
                
            """;

}
