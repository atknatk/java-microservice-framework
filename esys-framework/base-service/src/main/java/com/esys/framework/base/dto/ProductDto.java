package com.esys.framework.base.dto;

import com.esys.framework.base.currency.Moneys;
import com.esys.framework.base.entity.Product;
import com.esys.framework.core.dto.base.AbstractDto;
import com.esys.framework.core.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

/**
 * @author Atakan Atik (atakan.atik@everva.com.tr)

 */
@Data
public class ProductDto extends AbstractDto{


    private String name;
    private Moneys money;
    private float price;
    private float piece;

    public String getMoneyLocale(){
        if(money.equals(Moneys.US_DOLLAR)){
            return "ui.money.us";
        }else if(money.equals(Moneys.US_DOLLAR)){
            return "ui.money.tl";
        }else if(money.equals(Moneys.EURO)){
            return "ui.money.euro";
        }
        return "";
    }

    public ProductDto() {
    }

    public ProductDto(String name, Moneys money, float price, float piece) {
        this.name = name;
        this.money = money;
        this.price = price;
        this.piece = piece;
    }

    public ProductDto(Long id, String name, Moneys money, float price, float piece) {
        super(id);
        this.name = name;
        this.money = money;
        this.price = price;
        this.piece = piece;
    }

    public ProductDto(ProductDto dto) {
        super(dto.getId());
        this.name = dto.getName();
        this.money = dto.getMoney();
        this.price = dto.getPrice();
        this.piece = dto.piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDto)) return false;
        ProductDto that = (ProductDto) o;
        return Float.compare(that.getPrice(), getPrice()) == 0 &&
                Float.compare(that.getPiece(), getPiece()) == 0 &&
                Objects.equals(getName(), that.getName()) &&
                getMoney() == that.getMoney();
    }

    public float getTotal(){
        return piece * price;
    }

    public void setTotal(float total){
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }


    @Override
    public String toString() {
        return "ProductDto{" +
                "name='" + name + '\'' +
                ", money=" + money +
                ", price=" + price +
                ", piece=" + piece +
                '}';
    }
}
