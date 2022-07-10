package co.meli.cupon.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class BaseController {

  protected static final String SORT_DESCENDING = "desc";

  public BaseController() {}

  protected Pageable parsePageParams(String pPage, String pRows, String pField, String pDirection) {

    Sort.Order order = null;
    if (pField != null) {
      if (SORT_DESCENDING.equalsIgnoreCase(pDirection)) {
        order = Sort.Order.desc(pField);
      } else {
        order = Sort.Order.asc(pField);
      }
    }

    Sort sort = order != null ? Sort.by(order) : Sort.unsorted();
    Pageable pageable;
    if (StringUtils.isNumeric(pRows)) {
      if (StringUtils.isNumeric(pPage)) {
        pageable = PageRequest.of(Integer.parseInt(pPage), Integer.parseInt(pRows), sort);
      } else {
        pageable = PageRequest.of(0, Integer.parseInt(pRows), sort);
      }
    } else {
      pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
    }

    return pageable;
  }
}
