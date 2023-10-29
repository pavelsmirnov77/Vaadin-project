package ru.sovkombank.project.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;
import ru.sovkombank.project.services.UserService;
import ru.sovkombank.project.views.cart.CartView;
import ru.sovkombank.project.views.category.CategoryView;
import ru.sovkombank.project.views.header.HeaderView;
import ru.sovkombank.project.views.order.OrderView;
import ru.sovkombank.project.views.supplier.SupplierView;
import ru.sovkombank.project.views.user.UserInfoView;

public class MainLayout extends AppLayout {
    private H2 viewTitle;

    public MainLayout(UserService userService) {
        setPrimarySection(Section.DRAWER);
        addDrawerContent(userService);
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent(UserService userService) {
        H1 appName = new H1("Магазин");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        HeaderView header = new HeaderView(userService);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller);
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();
        nav.addItem(new SideNavItem("Профиль", UserInfoView.class, LineAwesomeIcon.USER.create()));
        nav.addItem(new SideNavItem("Список товаров", CategoryView.class, LineAwesomeIcon.PRODUCT_HUNT.create()));
        nav.addItem(new SideNavItem("Корзина", CartView.class, LineAwesomeIcon.CART_PLUS_SOLID.create()));
        nav.addItem(new SideNavItem("Заказы", OrderView.class, LineAwesomeIcon.MONEY_BILL_SOLID.create()));
        nav.addItem(new SideNavItem("Поставщики", SupplierView.class, LineAwesomeIcon.USER_ALT_SOLID.create()));

        return nav;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
