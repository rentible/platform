package hu.lanoga.flatshares.view;


import com.github.adminfaces.template.config.AdminConfig;
import com.github.adminfaces.template.config.ControlSidebarConfig;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.omnifaces.util.Faces;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LayoutTemplate implements Serializable {

    private static final Logger LOG = Logger.getLogger(LayoutTemplate.class.getName());
    public static final String ADMIN_XHTML = "/admin.xhtml";
    public static final String ADMIN_TOP_XHTML = "/admin-top.xhtml";
    public static final String TEMPLATES_TEMPLATE_XHTML = "/templates/template.xhtml";
    public static final String TEMPLATES_TEMPLATE_TOP_XHTML = "/templates/template-top.xhtml";

    private String template;
    private Boolean appTemplateExists;
    private Boolean leftMenuTemplate;
    private Boolean fixedLayout;
    private Boolean boxedLayout;
    private Boolean expandOnHover;
    private Boolean sidebarCollapsed;
    private Boolean fixedControlSidebar;
    private Boolean darkControlSidebarSkin;
    @Inject
    protected AdminConfig adminConfig;

    public LayoutTemplate() {
    }

    @PostConstruct
    public void init() {
        if (this.adminConfig.isLeftMenuTemplate()) {
            this.setDefaultTemplate();
        } else {
            this.setTemplateTop();
        }

        ControlSidebarConfig controlSidebarConfig = this.adminConfig.getControlSidebar();
        this.fixedLayout = controlSidebarConfig.getFixedLayout();
        this.boxedLayout = controlSidebarConfig.getBoxedLayout();
        this.expandOnHover = controlSidebarConfig.getExpandOnHover();
        this.sidebarCollapsed = controlSidebarConfig.getSidebarCollapsed();
        this.fixedControlSidebar = controlSidebarConfig.getFixed();
        this.darkControlSidebarSkin = controlSidebarConfig.getDarkSkin();
    }

    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setTemplateTop() {
        if (this.appTemplateExists()) {
            this.template = TEMPLATES_TEMPLATE_TOP_XHTML;
        } else {
            this.template = ADMIN_TOP_XHTML;
        }

        this.leftMenuTemplate = false;
    }

    public void setDefaultTemplate() {
        if (this.appTemplateExists()) {
            this.template = TEMPLATES_TEMPLATE_XHTML;
        } else {
            this.template = ADMIN_XHTML;
        }

        this.leftMenuTemplate = true;
    }

    public Boolean getLeftMenuTemplate() {
        return this.leftMenuTemplate;
    }

    public void setLeftMenuTemplate(Boolean leftMenuTemplate) {
        this.leftMenuTemplate = leftMenuTemplate;
    }

    public Boolean getFixedLayout() {
        return this.fixedLayout;
    }

    public void setFixedLayout(Boolean fixedLayout) {
        this.fixedLayout = fixedLayout;
    }

    public Boolean getBoxedLayout() {
        return this.boxedLayout;
    }

    public void setBoxedLayout(Boolean boxedLayout) {
        this.boxedLayout = boxedLayout;
    }

    public Boolean getExpandOnHover() {
        return this.expandOnHover;
    }

    public void setExpandOnHover(Boolean expandOnHover) {
        this.expandOnHover = expandOnHover;
    }

    public Boolean getSidebarCollapsed() {
        return this.sidebarCollapsed;
    }

    public void setSidebarCollapsed(Boolean sidebarCollapsed) {
        this.sidebarCollapsed = sidebarCollapsed;
    }

    public Boolean getFixedControlSidebar() {
        return this.fixedControlSidebar;
    }

    public void setFixedControlSidebar(Boolean fixedControlSidebar) {
        this.fixedControlSidebar = fixedControlSidebar;
    }

    public Boolean getDarkControlSidebarSkin() {
        return this.darkControlSidebarSkin;
    }

    public void setDarkControlSidebarSkin(Boolean darkControlSidebarSkin) {
        this.darkControlSidebarSkin = darkControlSidebarSkin;
    }

    public void toggleTemplate() {
        if (this.isDefaultTemplate()) {
            this.setTemplateTop();
        } else {
            this.setDefaultTemplate();
        }

    }

    public void toggleFixedLayout() {
        this.fixedLayout = !this.fixedLayout;
    }

    public void toggleBoxedLayout() {
        this.boxedLayout = !this.boxedLayout;
    }

    public void toggleExpandOnHover() {
        this.expandOnHover = !this.expandOnHover;
    }

    public void toggleSidebarCollapsed() {
        this.sidebarCollapsed = !this.sidebarCollapsed;
    }

    public void toggleFixedControlSidebar() {
        this.fixedControlSidebar = !this.fixedControlSidebar;
    }

    public void toggleControlSidebarSkin() {
        this.darkControlSidebarSkin = !this.darkControlSidebarSkin;
    }

    public boolean isDefaultTemplate() {
        return this.template != null && (this.template.endsWith("template.xhtml") || this.template.equals("admin.xhtml"));
    }

    private boolean appTemplateExists() {
        if (this.appTemplateExists != null) {
            return this.appTemplateExists;
        } else {
            try {
                this.appTemplateExists = Faces.getExternalContext().getResourceAsStream(TEMPLATES_TEMPLATE_XHTML) != null;
            } catch (Exception var2) {
                LOG.warning(String.format("Could not find application defined template in path '%s' due to following error: %s. Falling back to default admin template. See application template documentation for more details: https://github.com/adminfaces/admin-template#application-template", "/WEB-INF/templates/template.xhtml", var2.getMessage()));
                this.appTemplateExists = false;
            }

            return this.appTemplateExists;
        }
    }
}
