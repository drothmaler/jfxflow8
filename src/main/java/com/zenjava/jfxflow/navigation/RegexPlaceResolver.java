package com.zenjava.jfxflow.navigation;

import com.zenjava.jfxflow.actvity.HasNode;

import java.util.regex.Pattern;

/**
 * Provides an ActivityMapping strategy that tries to match a regular expression against Place names.
 */
public class RegexPlaceResolver implements PlaceResolver
{
    private String expression;
    private HasNode node;
    private Pattern pattern;

    /**
     * Constructs a new mapping between the specified regular expression and the specified Activity.
     *
     * @param expression the regular expression to use for this mapping. This can be any valid Java regular expression
     * and will be matched against Place names in the isMatch() method.
     * @param node the Node (owner) that the expression maps to.
     */
    public RegexPlaceResolver(String expression, HasNode node)
    {
        this.expression = expression;
        this.node = node;
        pattern = Pattern.compile(expression);
    }

    /**
     * Decides whether the specified Place is a match for this mapping by using a regular expression matching against
     * the name of the place and the regular expression for this mapping. If the expression can be matched to the place
     * name, then this method will return true.
     *
     * @param place the Place to match to this mapping.
     * @return true if the name of the specified Place is matched by this mappings regular expression.
     */
    public HasNode resolvePlace(Place place)
    {
        return pattern.matcher(place.getName()).matches() ? node : null;
    }

    /**
     * Retrieves the regular expression used by this mapping.
     *
     * @return the regular expression used by this mapping.
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * Retrieves the Node that this mapping is for.
     *
     * @return the Node that this mapping is for.
     */
    public HasNode getNode()
    {
        return node;
    }
}
